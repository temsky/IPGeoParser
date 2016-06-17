package ru.temsky.ipgeo.service;

import java.io.IOException;
import java.net.MalformedURLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

@Component
public class PageParserBrowser implements PageParser {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	static WebClient webClient = new WebClient(BrowserVersion.getDefault());

	static {
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.getOptions().setCssEnabled(false);
	}

	@Override
	public String parse(String url) {
		String result = "";
		logger.info("\nSending GET to: " + url);
		webClient.setAjaxController(new NicelyResynchronizingAjaxController());

		try {
			HtmlPage currentPage = webClient.getPage(url);
			result = currentPage.asXml();
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		webClient.close();
		return result;
	}

}
