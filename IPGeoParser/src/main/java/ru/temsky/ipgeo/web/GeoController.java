package ru.temsky.ipgeo.web;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

	@MessageMapping("/myip")
	@SendToUser("/topic/ip")
	public IP getIP(SimpMessageHeaderAccessor ha) throws Exception {
		String addr = ha.getSessionAttributes().get("ip").toString();
		IP result = new IP(addr);
		return result;
	}

	@MessageMapping("/myipinfo")
	@SendToUser("/topic/ipinfo")
	public IP getIPinfo(SimpMessageHeaderAccessor ha) throws Exception {
		String addr = ha.getSessionAttributes().get("ip").toString();
		IP result = geoService.start(addr);
		return result;
	}

	@RequestMapping(value = "/check", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<IP> getIPInfo(@RequestParam("ip") String adr, HttpServletResponse response) {
		IP result = geoService.start(adr);
		if (result == null)
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		return ResponseEntity.ok(result);
	}

}
