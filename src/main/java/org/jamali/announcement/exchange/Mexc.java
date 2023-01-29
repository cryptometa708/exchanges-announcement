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
public class Mexc extends Exchange {

	public Mexc(Client client, Properties properties) {
		super(client, properties);
		setCondition("title");
	}

	public String request() {
		Map<String, String> headers = new Hashtable<String, String>();
		headers.put("App-Language", "English");
		headers.put("Language", "en-US");
		headers.put("X-Mxc-Nonce", "1642018989217");
		headers.put("Platform", "android");
		headers.put("Type", "0");
		headers.put("User-Agent", Util.USER_AGENT);
		headers.put("Version", "3.0.8");
		headers.put("Accept-Encoding", "gzip, deflate");
		try {

			return this.getClient().doGet(this.getUrl(), "https://www.google.com", headers).getResponse();
		} catch (Exception e) {
			return null;
		}
	}

	public HashSet<Model> parse(String announcements) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode jsonNode = mapper.readTree(announcements);
			HashSet<Model> newAnnouncements = new HashSet<Model>();
			for (JsonNode node : jsonNode.get("msg")) {
				String title = node.get("title").asText();
				String code = node.get("id").asText();
				newAnnouncements.add(new Model("https://support.mexc.com/hc/en-001/articles/" + code, title));
			}
			return newAnnouncements;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}
}
