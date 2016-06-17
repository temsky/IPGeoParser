package ru.temsky.ipgeo.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.temsky.ipgeo.IP;

@Component
public class WhoisGeoiplookup implements Whois {
	private final PageParser pageParserJsoup;

	@Autowired
	public WhoisGeoiplookup(PageParser pageParserJsoup) {
		this.pageParserJsoup = pageParserJsoup;
	}

	@Override
	public IP whois(IP ip) {
		if (ip.getProvider().equals("RESERVED") || !ip.getCity().isEmpty())
			return ip;

		Pattern pattern;
		Matcher matcher;
		String source = pageParserJsoup.parse("http://api.geoiplookup.net/?query=" + ip.getAddress());
		pattern = Pattern.compile("<isp>(.+)</isp>", Pattern.CASE_INSENSITIVE);
		matcher = pattern.matcher(source);
		if (matcher.find()) {
			String prov = matcher.group(1).trim();
			ip.setProvider(prov);
		}

		pattern = Pattern.compile("<countrycode>(.+)</countrycode>", Pattern.CASE_INSENSITIVE);
		matcher = pattern.matcher(source);
		if (matcher.find()) {
			String country = matcher.group(1).trim();
			ip.setCountry(country);
		}

		pattern = Pattern.compile("<city>(.+)</city>", Pattern.CASE_INSENSITIVE);
		matcher = pattern.matcher(source);
		if (matcher.find()) {
			String city = matcher.group(1).trim();
			ip.setCity(city);
		}
		return ip;
	}

}
