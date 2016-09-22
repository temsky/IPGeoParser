package ru.temsky.ipgeo.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.temsky.ipgeo.IP;

@Component
public class WhoisIPGeoBase implements Whois {
	private final PageParser pageParserJsoup;

	@Autowired
	public WhoisIPGeoBase(PageParser pageParserJsoup) {
		this.pageParserJsoup = pageParserJsoup;
	}

	@Override
	public void whois(IP ip) {
		if (ip.getProvider().equals("RESERVED") || ip.getCountry().isEmpty() || !ip.getCity().isEmpty())
			return;

		String source = pageParserJsoup.parse("http://www.ipgeobase.ru:7020/geo?ip=" + ip.getAddress());
		Pattern pattern = Pattern.compile("<city>(.*?)</city>", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(source);
		if (matcher.find()) {
			String city = matcher.group(1);
			ip.setCity(city);
		}
	}

}
