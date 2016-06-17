package ru.temsky.ipgeo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import ru.temsky.ipgeo.IP;

@Component
public class ListCreatorImpl implements ListCreator {

	@Override
	public List<IP> getList(String data) {
		List<IP> list = new ArrayList<>();
		Pattern pattern = Pattern.compile("(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)");
		Matcher matcher = pattern.matcher(data);
		while (matcher.find())
			list.add(new IP(matcher.group()));

		return list;
	}

}
