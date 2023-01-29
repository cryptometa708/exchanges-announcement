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
public class ByBit extends Exchange {

	public ByBit(Client client, Properties properties) {
		super(client, properties);
		setCondition("entries");
	}

	public String request() {
		Map<String, String> headers = new Hashtable<String, String>();
		headers.put("Host", "cdn.contentstack.io");
		headers.put("Accept", "application/json, text/plain, */*");
		headers.put("Accept-Language", "en-US,en;q=0.5");
		headers.put("Accept-Encoding", "gzip, deflate, br");
		headers.put("api_key", "bltd582a520b3ab6888");
		headers.put("access_token", "cs0101c906be2817aebe6d9d36");
		headers.put("Origin", "https://blog.bybit.com");
		headers.put("DNT", "1");
		headers.put("Connection", "keep-alive");
		headers.put("Referer", "https://blog.bybit.com/");
		headers.put("Sec-Fetch-Dest", "empty");
		headers.put("Sec-Fetch-Mode", "cors");
		headers.put("Sec-Fetch-Site", "cross-site");
		headers.put("TE", "trailers");
		headers.put("User-Agent", Util.USER_AGENT);
		try {
			return getClient().doGet(this.getUrl(), "https://google.fr", headers).getResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public HashSet<Model> parse(String announcements) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode jsonNode = mapper.readTree(announcements);
			HashSet<Model> newAnnouncements = new HashSet<Model>();
			for (JsonNode node : jsonNode.get("entries")) {
				String code = node.get("uid").asText();
				newAnnouncements
						.add(new Model("https://blog.bybit.com/post/" + code, node.get("title").asText().trim()));
			}
			return newAnnouncements;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}
}
