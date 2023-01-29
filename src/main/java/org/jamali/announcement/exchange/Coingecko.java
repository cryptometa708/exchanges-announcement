package org.jamali.announcement.exchange;

import java.io.IOException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import org.jamali.announcement.utils.Client;
import org.jamali.announcement.utils.Model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * 
 * @author Ibrahim Jamali
 *
 */
public class Coingecko extends Exchange {

	public Coingecko(Client client, Properties properties) {
		super(client, properties);
		this.setCondition("data");
	}

	public String request() {
		return this.get(this.getUrl());
	}

	private String get(String url) {
		Map<String, String> headers = new Hashtable<String, String>();
		headers.put("Accept", "application/json, text/javascript, */*; q=0.01");
		headers.put("X-Requested-With", "XMLHttpRequest");
		headers.put("Accept-Encoding", "gzip, deflate");
		headers.put("Accept-Language", "en-US");
		headers.put("Platform", "android");
		headers.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:97.0) Gecko/20100101 Firefox/97.0");
		try {

			return this.getClient().doGet(url, "https://google.com", headers).getResponse();
		} catch (Exception e) {
			return null;
		}
	}

	public HashSet<Model> parse(String announcements) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode jsonNode = mapper.readTree(announcements);
			HashSet<Model> newAnnouncements = new HashSet<Model>();
//			double price = jsonNode.get("market_data").asDouble(0);

			for (JsonNode node : jsonNode) {
				String title = "Coingecko listed " + node.get("name").asText() + "(" + node.get("symbol").asText()
						+ ")";
				String code = node.get("id").asText();
				newAnnouncements.add(new Model("https://www.coingecko.com/en/coins/" + code, title));
			}
			return newAnnouncements;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

}
