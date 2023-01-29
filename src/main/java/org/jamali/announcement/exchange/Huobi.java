package org.jamali.announcement.exchange;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import org.jamali.announcement.utils.Client;
import org.jamali.announcement.utils.Model;
import org.jamali.announcement.utils.Util;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/**
 * 
 * @author Ibrahim Jamali
 *
 */
public class Huobi extends Exchange {

	public Huobi(Client client, Properties properties) {
		super(client, properties);
		setCondition("Announcements");
	}

	public String request() {
		Map<String, String> headers = new Hashtable<String, String>();
		headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8");
		headers.put("Accept-Language", "en-US,en;q=0.5");
		headers.put("Alt-Used", "www.huobi.com");
		headers.put("Cache-Control", "max-age=0");
		headers.put("Connection", "keep-alive");
		headers.put("Upgrade-Insecure-Requests", "1");
		headers.put("User-Agent", Util.USER_AGENT);
		try {

			return this.getClient().doGet(this.getUrl(), "https://www.huobi.com/", headers).getResponse();
		} catch (Exception e) {
			return null;
		}
	}

	public HashSet<Model> parse(String announcements) {

		Document doc = Jsoup.parse(announcements);
		HashSet<Model> models = new HashSet<Model>();
		Elements news = doc.select(".link-dealpair a");
		for (Element e : news) {
			models.add(new Model("https://www.huobi.com" + e.attr("href"), e.text()));
		}
		return models;
	}

}
