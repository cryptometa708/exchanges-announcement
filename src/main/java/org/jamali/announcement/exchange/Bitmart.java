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
public class Bitmart extends Exchange {

	public Bitmart(Client client, Properties properties) {
		super(client, properties);
		this.setCondition("breadcrumbs");
	}

	public String request() {
		Map<String, String> headers = new Hashtable<String, String>();
		headers.put("Accept", "application/json, text/javascript, */*; q=0.01");
		headers.put("X-Requested-With", "XMLHttpRequest");
		headers.put("Accept-Encoding", "gzip, deflate");
		headers.put("Accept-Language", "en-US");
		headers.put("If-None-Match", "W/\"97d55be6c6bab04f014370a720ba1069\"");
		headers.put("Platform", "android");
		headers.put("User-Agent",
				"BitMart/2.8.3 (com.bitmart.exchange; build:143; Android 7.1.2; device:i_331542119783448; deviceName: ASUS_Z01QD)Mozilla/5.0 (Linux; Android 7.1.2; ASUS_Z01QD Build/N2G48H; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/68.0.3440.70 Mobile Safari/537.36;webank/h5face;webank/1.0;netType:NETWORK_WIFI;appVersion:143;packageName:com.bitmart.bitmarket");
		try {

			return this.getClient().doGet(this.getUrl(), "https://support.bmx.fund/hc/en-us", headers).getResponse();
		} catch (Exception e) {
			return null;
		}
	}

	public HashSet<Model> parse(String announcements) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode jsonNode = mapper.readTree(announcements);
			HashSet<Model> newAnnouncements = new HashSet<Model>();
			for (JsonNode node : jsonNode.get("activities")) {
				String title = node.get("title").asText();
				String code = node.get("url").asText();
				newAnnouncements.add(new Model("https://support.bmx.fund" + code, title));
			}
			return newAnnouncements;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

}
