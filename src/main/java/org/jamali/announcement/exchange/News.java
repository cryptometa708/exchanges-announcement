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
public class News extends Exchange {

	public News(Client client, Properties properties) {
		super(client, properties);
		setCondition("https");
	}

	public String request() {
		Map<String, String> headers = new Hashtable<String, String>();
		headers.put("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
		headers.put("Accept-Language", "en-GB,en;q=0.9,en-US;q=0.8,fr;q=0.7,ar;q=0.6,es;q=0.5,de;q=0.4");
		headers.put("Cache-Control", "max-age=0");
		headers.put("Connection", "keep-alive");
		headers.put("Upgrade-Insecure-Requests", "1");
		headers.put("User-Agent", Util.USER_AGENT);
		try {
			return this.getClient().doGet(this.getUrl(), "https://www.bitfinex.com/", headers).getResponse();
		} catch (Exception e) {
			return null;
		}
	}

	public HashSet<Model> parse(String announcements) {
		ObjectMapper mapper = new ObjectMapper();

		try {
			JsonNode entries = mapper.readTree(announcements);
			HashSet<Model> newAnnouncements = new HashSet<Model>();
			for (JsonNode node : entries) {
				String code = node.get(0).asText();
				String title = node.get(5).asText();
				newAnnouncements.add(new Model("https://pulse.bitfinex.com/post/lending/" + code, title));
			}
			return newAnnouncements;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}
}
