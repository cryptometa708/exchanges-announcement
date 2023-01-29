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
public class Coinmarketcap extends Exchange {

	public Coinmarketcap(Client client, Properties properties) {
		super(client, properties);
		this.setCondition("data");
	}

	public String request() {
		Map<String, String> headers = new Hashtable<String, String>();
		headers.put("Accept", "application/json, text/javascript, */*; q=0.01");
		headers.put("X-Requested-With", "XMLHttpRequest");
		headers.put("Accept-Encoding", "gzip, deflate");
		headers.put("Accept-Language", "en-US");
		headers.put("Platform", "android");
		headers.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:97.0) Gecko/20100101 Firefox/97.0");
		try {

			return this.getClient().doGet(this.getUrl(), "https://google.com", headers).getResponse();
		} catch (Exception e) {
			return null;
		}
	}

	public HashSet<Model> parse(String announcements) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode jsonNode = mapper.readTree(announcements);
			HashSet<Model> newAnnouncements = new HashSet<Model>();
			for (JsonNode node : jsonNode.get("data")) {
				String code = node.get("slug").asText();

				JsonNode platform = node.get("platform");

				StringBuffer title = new StringBuffer("Coinmarketcap listed ").append(node.get("name").asText())
						.append("(").append(node.get("symbol").asText()).append(")%return%%return%");
				if (!platform.isNull()) {
					String ecosystem = platform.get("name").asText();
					String contract = platform.get("token_address").asText();
					title.append("%return%📦 <b>Ecosystem : </b>").append(ecosystem);
					title.append("%return%📃 <b>contract : </b>").append(contract);
				}
				title.append("%return%<b>🏷️ Price :</b>")
						.append(node.get("quote").get("USD").get("price").asDouble(0));
				newAnnouncements.add(new Model("https://coinmarketcap.com/currencies/" + code, title.toString()));
			}
			return newAnnouncements;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

}
