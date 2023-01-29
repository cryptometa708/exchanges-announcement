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
public class Mandala extends Exchange {

	public Mandala(Client client, Properties properties) {
		super(client, properties);
		setCondition("msg\"\\:\"Success");
	}

	public String request() {
		Map<String, String> headers = new Hashtable<String, String>();
		headers.put("Cookie", "XFGRE=GHEIW");
		headers.put("X-App-Id", "poloniex-android");
		headers.put("X-App-Version", "1.32.0");
		headers.put("X-Requested-With", "XMLHttpRequest");
		headers.put("Device-Info",
				"eyJzeXN0ZW1WZXJzaW9uIjoiQW5kcm9pZCA3LjEuMiIsInN5c3RlbUxhbmciOiJlbiIsImRldmljZU5hbWUiOiJBU1VTX1owMVFEIiwidGltZXpvbmVPZmZzZXQiOi00ODAsInRpbWV6b25lIjoiR01UKzA4OjAwIn0=");
		headers.put("Deviceno", "174c62989d8700d951698c6c8ee1f979");
		headers.put("X-Group", "poloniex");
		headers.put("User-Agent", "TWFuZGFsYSgxLjAuMCk7QVNVU19aMDFRRChBbmRyb2lkIDcuMS4yKQ==");
		headers.put("Connection", "close");
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
