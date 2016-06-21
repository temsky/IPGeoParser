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
	public IP whois(IP ip) {
		if (ip.getProvider().equals("RESERVED") || !ip.getCountry().isEmpty())
			return ip;

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
						String net = RangeToCidr(matcher.group(1), matcher.group(2));
						String subnet = net.split("/")[0];
						String cidr = net.split("/")[1];
						ip.setSubnet(subnet);
						ip.setCidr(cidr);

					}
				}
			}
		}

		return ip;
	}

	private String RangeToCidr(String firstIp, String lastIp) {
		Long ip1 = ipToLong(firstIp);
		Long ip2 = ipToLong(lastIp);

		// Determine all bits that are different between the two IPs
		Long diffs = ip1 ^ ip2;

		// Now count the number of consecutive zero bits starting at the most
		// significant
		int bits = 32;
		int mask = 0;
		while (diffs != 0) {
			// We keep shifting diffs right until it's zero (i.e. we've shifted
			// all the non-zero bits off)
			diffs >>= 1;
			// Every time we shift, that's one fewer consecutive zero bits in
			// the prefix
			bits--;
			// Accumulate a mask which will have zeros in the consecutive zeros
			// of the prefix and ones elsewhere
			mask = (mask << 1) | 1;
		}

		// Construct the root of the range by inverting the mask and ANDing it
		// with the start address
		Long root = ip1 & ~mask;
		// Finally, output the range

		String output = longToIP(root) + "/" + bits;
		return output;
	}

	private static long ipToLong(String strIP) {
		long[] ip = new long[4];
		String[] ipSec = strIP.split("\\.");
		for (int k = 0; k < 4; k++) {
			ip[k] = Long.valueOf(ipSec[k]);
		}
		return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
	}

	private static String longToIP(long longIP) {
		StringBuffer sb = new StringBuffer("");
		sb.append(String.valueOf(longIP >>> 24));
		sb.append(".");
		sb.append(String.valueOf((longIP & 0x00FFFFFF) >>> 16));
		sb.append(".");
		sb.append(String.valueOf((longIP & 0x0000FFFF) >>> 8));
		sb.append(".");
		sb.append(String.valueOf(longIP & 0x000000FF));
		return sb.toString();
	}

}
