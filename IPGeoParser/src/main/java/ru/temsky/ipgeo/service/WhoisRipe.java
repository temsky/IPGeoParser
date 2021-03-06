package ru.temsky.ipgeo.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.temsky.ipgeo.IP;

@Component
public class WhoisRipe implements Whois {
	private final PageParser pageParserJsoup;

	@Autowired
	public WhoisRipe(PageParser pageParserJsoup) {
		this.pageParserJsoup = pageParserJsoup;
	}

	@Override
	public void whois(IP ip) {
		if (ip.getProvider().equals("RESERVED") || !ip.getCountry().isEmpty())
			return;

		Pattern pattern;
		Matcher matcher;
		String source = pageParserJsoup.parse("http://rest.db.ripe.net/search.xml?query-string=" + ip.getAddress() + "&flags=resource");
		pattern = Pattern.compile("<attribute name=\"country\" value=\"(\\w+)\"", Pattern.CASE_INSENSITIVE);
		matcher = pattern.matcher(source);
		if (matcher.find()) {
			String country = matcher.group(1);
			if (!country.equals("EU")) {
				ip.setCountry(country);
				pattern = Pattern.compile("<attribute name=\"netname\" value=\"(.*)\"\\s*/>", Pattern.CASE_INSENSITIVE);
				matcher = pattern.matcher(source);
				if (matcher.find()) {
					ip.setProvider(matcher.group(1));
				}

				pattern = Pattern.compile("<object type=\"route\">.*</object>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
				matcher = pattern.matcher(source);
				if (matcher.find()) {
					source = matcher.group();
					pattern = Pattern.compile("<attribute name=\"route\" value=\"(\\d+.\\d+.\\d+.\\d+)/(\\d+)\"\\s*/>", Pattern.CASE_INSENSITIVE);
					matcher = pattern.matcher(source);
					pattern = Pattern.compile("<attribute name=\"descr\" value=\"(.*?)\"\\s*/>", Pattern.CASE_INSENSITIVE);
					Matcher mProv = pattern.matcher(source);

					if (matcher.find() && mProv.find()) {
						String subnet = matcher.group(1);
						String cidr = matcher.group(2);
						String prov = mProv.group(1).replaceAll("&quot;", "");
						// cidr must be > 8, otherwise data may be incorrect
						if (Integer.parseInt(cidr) > 8) {
							ip.setSubnet(subnet);
							ip.setCidr(cidr);
							// Rewrite provider
							ip.setProvider(prov);
						}
					}
				} else {
					pattern = Pattern.compile("inetnum\"\\s*value=\"(\\d+.\\d+.\\d+.\\d+)\\s*-\\s*(\\d+.\\d+.\\d+.\\d+)", Pattern.CASE_INSENSITIVE);
					matcher = pattern.matcher(source);
					if (matcher.find()) {
						String net = IPUtils.RangeToCidr(matcher.group(1), matcher.group(2));
						String subnet = net.split("/")[0];
						String cidr = net.split("/")[1];
						ip.setSubnet(subnet);
						ip.setCidr(cidr);

					}
				}
			}
		}
	}

}
