package ru.temsky.ipgeo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.temsky.ipgeo.IP;
import ru.temsky.ipgeo.domain.Subnet;
import ru.temsky.ipgeo.domain.SubnetRepository;

@Component
public class StorageImpl implements Storage {

	@Autowired
	SubnetRepository subnetRepository;

	@Override
	public void check(IP ip) {
		Subnet sub = subnetRepository.findSubnet(ip.getAddress());
		if (sub != null) {
			ip.setCountry(sub.getCountry());
			ip.setCity(sub.getCity());
			ip.setProvider(sub.getProvider());
			String subnet = sub.getName().split("/")[0];
			String cidr = sub.getName().split("/")[1];
			ip.setSubnet(subnet);
			ip.setCidr(cidr);
		}
	}

	@Override
	public synchronized void save(IP ip) {
		if (ip.getSubnet() != "") {
			String sub = ip.getSubnet() + "/" + ip.getCidr();
			if (!subnetRepository.exists(sub)) {
				subnetRepository.save(new Subnet(sub, ip.getCountry(), ip.getCity(), ip.getProvider()));
			}
		}
	}

}
