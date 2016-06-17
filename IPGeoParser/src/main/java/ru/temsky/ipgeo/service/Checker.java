
package ru.temsky.ipgeo.service;

import ru.temsky.ipgeo.IP;

public interface Checker {
	IP checkLocal(IP ip);

	boolean compare(IP newIP, IP oldIP);

	IP copy(IP newIP, IP oldIP);
}
