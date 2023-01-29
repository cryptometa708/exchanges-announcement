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
public class KuCoin extends Exchange {

	public KuCoin(Client client, Properties properties) {
		super(client, properties);
		setCondition("success\":true");
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

			return this.getClient().doGet(this.getUrl(), "https://blog.kuCoin.com/", headers).getResponse();
		} catch (Exception e) {
			return null;
		}
	}

	public HashSet<Model> parse(String announcements) {

		ObjectMapper mapper = new ObjectMapper();

		try {
			JsonNode jsonNode = mapper.readTree(announcements);
			HashSet<Model> newAnnouncements = new HashSet<Model>();
			for (JsonNode node : jsonNode.get("items")) {
				String code = node.get("path").asText().trim();
				String title = node.get("title").asText().trim();
				if (title != null && code != null && !title.equals("") && !code.equals(""))
					newAnnouncements.add(new Model("https://www.kucoin.com/news" + code, title));
			}
			return newAnnouncements;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}
}
