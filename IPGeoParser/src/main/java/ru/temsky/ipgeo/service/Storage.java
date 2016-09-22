package ru.temsky.ipgeo.service;

import ru.temsky.ipgeo.IP;

public interface Storage {
	void check(IP ip);

	void save(IP ip);
}
