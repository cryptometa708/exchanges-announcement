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
public class Poloniex extends Exchange {

	public Poloniex(Client client, Properties properties) {
		super(client, properties);
		setCondition("success\"\\:1");
	}

	public String request() {
		Map<String, String> headers = new Hashtable<String, String>();
		headers.put("Cookie", "XFGRE=GHEIW");
		headers.put("X-App-Id", "poloniex-android");
		headers.put("X-App-Version", "1.32.0");
		headers.put("X-Requested-With", "XMLHttpRequest");
		headers.put("X-Device-Id",
				"{\"aid\":\"\",\"deviceOs\":\"Android\",\"deviceOsVersion\":\"7.1.2\",\"did\":\"2743739ee8dc2158\",\"fingerprint\":\"70efed09-cb5e-42d0-84f8-fee69d294d7b\",\"firebaseId\":\"\",\"manufacturer\":\"Asus\",\"model\":\"ASUS_Z01QD\",\"modelVersion\":\"ASUS_Z01QD\",\"tz\":\"+0800\"}");
		headers.put("X-Session-Token", "");
		headers.put("X-Group", "poloniex");
		headers.put("Accept", "*/*");
		headers.put("User-Agent", "Android 7.1.2; Mobile");
		headers.put("Accept-Language", "en-US");
		headers.put("Accept-Encoding", "gzip, deflate");
		try {

			return this.getClient().doGet(this.getUrl(), "https://poloniex.com", headers).getResponse();
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
			for (JsonNode node : jsonNode.get("response")) {
				String title = node.get("title").asText();
				String code = Util.toSlug(title);
				newAnnouncements.add(new Model(
						"https://support.poloniex.com/hc/en-us/sections/360006455214-New-Coin-Listings?" + code,
						title));
			}
			return newAnnouncements;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
