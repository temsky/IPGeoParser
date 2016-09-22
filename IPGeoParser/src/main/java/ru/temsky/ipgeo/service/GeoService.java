package ru.temsky.ipgeo.service;

import java.util.List;

import ru.temsky.ipgeo.IP;

public interface GeoService {
	List<IP> start(String data, String idSession);

	IP start(String data);
}
