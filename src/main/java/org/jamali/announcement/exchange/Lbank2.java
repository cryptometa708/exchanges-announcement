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
public class Lbank2 extends Exchange {

	public Lbank2(Client client, Properties properties) {
		super(client, properties);
		setCondition("resultList");
	}

	public String request() {
		Map<String, String> headers = new Hashtable<String, String>();
		headers.put("X-Xx-Apppackagename", "com.superchain.lbankgoogle");
		headers.put("X-Xx-Sdkversioncode", "25");
		headers.put("X-Xx-Manufacturer", "Asus");
		headers.put("X-Xx-Device", "2743739ee8dc2158");
		headers.put("X-Xx-Net-Provider", "Orange");
		headers.put("X-Xx-Net-Env", "det net type");
		headers.put("X-Xx-Platform-Source", "ANDROID");
		headers.put("X-Xx-Os-Version", "7.1.2");
		headers.put("X-Xx-Device-Name", "ASUS_Z01QD");
		headers.put("X-Xx-Version-Code", "1000005115");
		headers.put("X-Xx-Version", "3.41.39");
		headers.put("X-Xx-Channel", "def channel");
		headers.put("Accept-Language", "en-US");
		headers.put("Content-Type", "application/x-www-form-urlencoded");
		headers.put("Source", "1");
		headers.put("Google", "b368c8fe485d497a896d67aaaa3d1529f7178cb1a23247cf8ab0a60265e378b7");
		headers.put("User-Agent", "LBank/Android/V2/3.41.39");
		headers.put("Accept-Encoding", "gzip, deflate");
		try {

			return this.getClient().doGet(this.getUrl(), "https://www.google.com", headers, "okex.cookies")
					.getResponse();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public HashSet<Model> parse(String announcements) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode jsonNode = mapper.readTree(announcements);
			HashSet<Model> newAnnouncements = new HashSet<Model>();
			for (JsonNode node : jsonNode.get("data").get("list").get("resultList")) {
				String title = node.get("infoTitle").asText();
				String code = node.get("url").asText();
				newAnnouncements.add(new Model(code, title));
			}
			return newAnnouncements;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
