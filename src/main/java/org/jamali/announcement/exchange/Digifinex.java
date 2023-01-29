package org.jamali.announcement.exchange;

import java.io.IOException;
import java.util.Arrays;
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
public class Digifinex extends Exchange {

	public Digifinex(Client client, Properties properties) {
		super(client, properties);
		setCondition("errcode\"\\:0");
	}

	public String request() {
		Map<String, String> headers = new Hashtable<String, String>();
		headers.put("Dcsc", "97470bb003227db233cd25a0948268bd");
		headers.put("Dcts", "1642936112");
		headers.put("Lang", "en-ww");
		headers.put("Device-Uuid", "f16aeb782462e7431ca9b73f398e463e");
		headers.put("Content-Type", "application/x-www-form-urlencoded");
		headers.put("Accept-Encoding", "gzip, deflate");
		try {
			String response = this.getClient().doPost(this.getUrl(), null, Arrays.asList(), headers).getResponse();
			return response;
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
			for (JsonNode node : jsonNode.get("data").get("list")) {
				String title = node.get("title").asText();
				String code = node.get("url").asText();
				newAnnouncements.add(new Model(code, title));
			}
			return newAnnouncements;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
