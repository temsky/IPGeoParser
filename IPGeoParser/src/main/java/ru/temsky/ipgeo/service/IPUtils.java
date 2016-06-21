package ru.temsky.ipgeo.service;

public class IPUtils {
	public static String RangeToCidr(String firstIp, String lastIp) {
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

	public static long ipToLong(String strIP) {
		long[] ip = new long[4];
		String[] ipSec = strIP.split("\\.");
		for (int k = 0; k < 4; k++) {
			ip[k] = Long.valueOf(ipSec[k]);
		}
		return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
	}

	public static String longToIP(long longIP) {
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
