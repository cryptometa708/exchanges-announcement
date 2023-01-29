package org.jamali.announcement.exchange;

import java.io.IOException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import org.jamali.announcement.utils.Client;
import org.jamali.announcement.utils.Model;
import org.jamali.announcement.utils.Util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * 
 * @author Ibrahim Jamali
 *
 */
public class Binance extends Exchange {

	public Binance(Client client, Properties properties) {
		super(client, properties);
		setCondition("success\":true");
	}

	public String request() {
		Map<String, String> headers = new Hashtable<String, String>();
		headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		headers.put("Accept-Encoding", "gzip, deflate, br");
		headers.put("Accept-Language", "en-US,en;q=0.5");
		headers.put("Cache-Control", "max-age=0");
		headers.put("Connection", "keep-alive");
		headers.put("Upgrade-Insecure-Requests", "1");
		headers.put("User-Agent", Util.USER_AGENT);
		try {
			return getClient().doGet(this.getUrl(), "https://google.fr", headers).getResponse();
		} catch (Exception e) {
			return null;
		}
	}
	public HashSet<Model> parse(String announcements) {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode;
		try {
			jsonNode = mapper.readTree(announcements);
			HashSet<Model> newAnnouncements = new HashSet<Model>();
			for (JsonNode node : jsonNode.get("data").get("articles")) {
				String code = node.get("code").asText();
				String title = node.get("title").asText();
				newAnnouncements.add(new Model("https://www.binance.com/en/support/announcement/" + code, title));
			}
			return newAnnouncements;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}
}
