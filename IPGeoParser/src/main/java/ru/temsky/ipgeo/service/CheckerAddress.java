package ru.temsky.ipgeo.service;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.stereotype.Component;

import ru.temsky.ipgeo.IP;

@Component
public class CheckerAddress implements Checker {

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
		long ip = IPUtils.ipToLong(strIP);
		long subnet = IPUtils.ipToLong(strSubnet);
		long subnetMask = Long.MAX_VALUE << (32 - Integer.parseInt(cidr));
		if ((ip & subnetMask) == subnet)
			return true;
		else
			return false;
	}

}
