package ru.temsky.ipgeo.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import ru.temsky.ipgeo.IP;
import ru.temsky.ipgeo.IncomingData;
import ru.temsky.ipgeo.service.GeoService;

@Controller
public class GeoController {
	private final GeoService geoService;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public GeoController(GeoService geoService) {
		this.geoService = geoService;
	}

	@MessageMapping("/data")
	@SendToUser("/topic/result")
	public List<IP> getResult(IncomingData message, SimpMessageHeaderAccessor headerAccessor) throws Exception {
		long start = System.currentTimeMillis();
		String idSession = headerAccessor.getSessionId();
		List<IP> resultList = geoService.start(message.getData(), idSession);
		long finish = System.currentTimeMillis();
		long timeConsumedMillis = finish - start;
		// logger.info("time: " + timeConsumedMillis);
		return resultList;
	}

}
