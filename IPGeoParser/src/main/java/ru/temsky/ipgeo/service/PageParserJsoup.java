package ru.temsky.ipgeo.service;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PageParserJsoup implements PageParser {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0";

	private final static int COUNT_OF_TRY = 3;

	@Override
	public String parse(String url) {
		String result = "";
		try {
			Connection.Response response = null;

			int statusCode = 0;
			for (int i = 0; i < COUNT_OF_TRY; i++) {
				response = Jsoup.connect(url).userAgent(USER_AGENT).ignoreHttpErrors(true).timeout(5000).execute();
				logger.info("\nSending GET to: " + url);
				statusCode = response.statusCode();
				logger.info("\nstatusCode  = " + statusCode);
				if (statusCode == 200) {
					Document doc = response.parse();
					doc.outputSettings(new Document.OutputSettings().prettyPrint(false));
					result = doc.outerHtml();
					return result;
				}

				Thread.sleep(500 * i);
				logger.info("\nRetry request..");
			}

			throw new Exception("Error code: " + statusCode);

		} catch (Exception ex) {
			ex.printStackTrace();
			return result;
		}

	}

}
