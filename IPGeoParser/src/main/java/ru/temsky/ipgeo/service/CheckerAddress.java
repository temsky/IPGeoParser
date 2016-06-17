package ru.temsky.ipgeo.service;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.stereotype.Component;

import ru.temsky.ipgeo.IP;

@Component
public class CheckerAddress implements Checker {

	public long ipToLong(String ip) {
		try {
			byte[] bytes = InetAddress.getByName(ip).getAddress();
			long oct1 = bytes[0] & 0xFF;
			oct1 <<= 24;
			long oct2 = bytes[1] & 0xFF;
			oct2 <<= 16;
			long oct3 = bytes[2] & 0xFF;
			oct3 <<= 8;
			long oct4 = bytes[3] & 0xFF;
			return oct1 | oct2 | oct3 | oct4;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return 0;
		}

	}

	@Override
	public IP checkLocal(IP ip) {
		try {
			if (ip.getProvider().isEmpty()) {
				String strIP = ip.getAddress();
				if (InetAddress.getByName(strIP).isSiteLocalAddress()) {
					ip.setProvider("RESERVED");
				} else if (subnetCheck(strIP, "127.0.0.0", "8") || subnetCheck(strIP, "169.254.0.0", "16") || subnetCheck(strIP, "224.0.0.0", "4") || subnetCheck(strIP, "240.0.0.0", "4")) {
					ip.setProvider("RESERVED");
				}
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return ip;
	}

	@Override
	public boolean compare(IP newIP, IP oldIP) {
		if (newIP.getAddress().equals(oldIP.getAddress()))
			return true;

		else if (subnetCheck(newIP.getAddress(), oldIP.getSubnet(), oldIP.getCidr()))
			return true;
		else
			return false;
	}

	@Override
	public IP copy(IP newIP, IP oldIP) {
		newIP.setCountry(oldIP.getCountry());
		newIP.setCity(oldIP.getCity());
		newIP.setProvider(oldIP.getProvider());
		newIP.setSubnet(oldIP.getSubnet());
		newIP.setCidr(oldIP.getCidr());
		return newIP;
	}

	private boolean subnetCheck(String strIP, String strSubnet, String cidr) {
		if (strSubnet.isEmpty() || cidr.isEmpty())
			return false;
		long ip = ipToLong(strIP);
		long subnet = ipToLong(strSubnet);
		long subnetMask = Long.MAX_VALUE << (32 - Integer.parseInt(cidr));
		if ((ip & subnetMask) == subnet)
			return true;
		else
			return false;
	}

}
