package ru.temsky.ipgeo.config;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

class IpHandshakeInterceptor implements HandshakeInterceptor {

	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
		ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
		HttpServletRequest req = servletRequest.getServletRequest();

		String ipAddress = req.getHeader("x-forwarded-for");
		if (ipAddress == null) {
			ipAddress = req.getHeader("X_FORWARDED_FOR");
			if (ipAddress == null) {
				ipAddress = req.getRemoteAddr();
			}
		}

		attributes.put("ip", ipAddress);
		return true;
	}

	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
	}
}
