package ru.temsky.ipgeo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class Session {
	private static List<String> listSession = new ArrayList<>();

	@EventListener
	public void onSocketDisconnected(SessionDisconnectEvent event) {
		StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
		listSession.remove(sha.getSessionId());
	}

	@EventListener
	public void onSocketConnected(SessionConnectedEvent event) {
		StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
		listSession.add(sha.getSessionId());
	}

	public static boolean isDie(String idSession) {
		if (!listSession.contains(idSession))
			return true;
		return false;
	}

	public static List<String> getList() {
		return listSession;
	}

}
