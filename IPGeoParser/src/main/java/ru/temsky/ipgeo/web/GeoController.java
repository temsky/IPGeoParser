package ru.temsky.ipgeo.web;

import java.util.List;

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

	@Autowired
	public GeoController(GeoService geoService) {
		this.geoService = geoService;
	}

	@MessageMapping("/data")
	@SendToUser("/topic/result")
	public List<IP> getResult(IncomingData message, SimpMessageHeaderAccessor headerAccessor) throws Exception {
		String idSession = headerAccessor.getSessionId();
		List<IP> resultList = geoService.start(message.getData(), idSession);
		return resultList;
	}

}
