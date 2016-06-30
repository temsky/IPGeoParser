package ru.temsky.ipgeo.service;

import ru.temsky.ipgeo.IP;

public interface Storage {
	IP check(IP ip);

	void save(IP ip);
}
