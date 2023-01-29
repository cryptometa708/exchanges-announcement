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
public class Coinbase extends Exchange {

	public Coinbase(Client client, Properties properties) {
		super(client, properties);
		setCondition("components");
	}

	public String request() {
		Map<String, String> headers = new Hashtable<String, String>();
		headers.put("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
		headers.put("Accept-Language", "en-US,en;q=0.5");
		headers.put("Accept-Encoding", "gzip, deflate, br");
		headers.put("DNT", "1");
		headers.put("Connection", "keep-alive");
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
			for (JsonNode node : jsonNode.get("incidents")) {
				String code = node.get("id").asText();
				newAnnouncements
						.add(new Model("https://twitter.com/CoinbaseAssets?" + code, node.get("name").asText().trim()));
			}
			return newAnnouncements;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

}
