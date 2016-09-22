package ru.temsky.ipgeo.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.temsky.ipgeo.IP;

@Component
public class WhoisPredator implements Whois {
	private final PageParser pageParserBrowser;

	@Autowired
	public WhoisPredator(PageParser pageParserBrowser) {
		this.pageParserBrowser = pageParserBrowser;
	}

	@Override
	public void whois(IP ip) {
		if (ip.getProvider().equals("RESERVED") || !ip.getCity().isEmpty())
			return;

		Pattern pattern;
		Matcher matcher;

		String source = pageParserBrowser.parse("http://api.predator.wtf/geoip/?arguments=" + ip.getAddress());

		pattern = Pattern.compile("Organization:\\s*([^<]+)", Pattern.CASE_INSENSITIVE);
		matcher = pattern.matcher(source);
		if (matcher.find()) {
			String prov = matcher.group(1).trim();
			ip.setProvider(prov);
		}

		pattern = Pattern.compile("Country Code:\\s*(\\w{2})", Pattern.CASE_INSENSITIVE);
		matcher = pattern.matcher(source);
		if (matcher.find()) {
			String country = matcher.group(1).trim();
			ip.setCountry(country);
		}

		pattern = Pattern.compile("City:\\s*([^<]+)", Pattern.CASE_INSENSITIVE);
		matcher = pattern.matcher(source);
		if (matcher.find()) {
			String city = matcher.group(1).trim();
			ip.setCity(city);
		}
	}

}
