package ru.temsky.ipgeo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.temsky.ipgeo.IP;

@Service
public class GeoServiceImpl implements GeoService {
	private final ListCreator listCreator;
	private final Whois whoisRipe;
	private final Whois whoisIPGeoBase;
	private final Whois whoisGeoiplookup;
	private final Whois whoisPredator;
	private final Checker checkerAddress;
	private final Storage storage;

	@Autowired
	public GeoServiceImpl(ListCreator listCreator, Whois whoisRipe, Whois whoisIPGeoBase, Whois whoisGeoiplookup, Whois whoisPredator, Checker checkerAddress, Storage storage) {
		this.listCreator = listCreator;
		this.whoisRipe = whoisRipe;
		this.whoisIPGeoBase = whoisIPGeoBase;
		this.whoisGeoiplookup = whoisGeoiplookup;
		this.whoisPredator = whoisPredator;
		this.checkerAddress = checkerAddress;
		this.storage = storage;
	}

	@Override
	public List<IP> start(String data, String idSession) {
		List<IP> ipList = listCreator.getList(data);

		for (int i = 0; i < ipList.size(); i++) {
			if (Session.isDie(idSession))
				return null;

			IP ip = ipList.get(i);
			checkerAddress.checkLocal(ip);
			storage.check(ip);
			whoisRipe.whois(ip);
			whoisIPGeoBase.whois(ip);
			whoisGeoiplookup.whois(ip);
			whoisPredator.whois(ip);

			for (int j = i + 1; j < ipList.size(); j++) {
				IP newIP = ipList.get(j);
				if (checkerAddress.compare(newIP, ip)) {
					newIP = checkerAddress.copy(newIP, ip);
				}
			}
			storage.save(ip);
		}

		return ipList;
	}

	@Override
	public IP start(String data) {
		List<IP> ipList = listCreator.getList(data);

		for (int i = 0; i < ipList.size(); i++) {

			IP ip = ipList.get(i);
			checkerAddress.checkLocal(ip);
			storage.check(ip);
			whoisRipe.whois(ip);
			whoisIPGeoBase.whois(ip);
			whoisGeoiplookup.whois(ip);
			whoisPredator.whois(ip);

			storage.save(ip);
		}
		if (ipList.size() == 0)
			return null;
		return ipList.get(0);
	}
}
