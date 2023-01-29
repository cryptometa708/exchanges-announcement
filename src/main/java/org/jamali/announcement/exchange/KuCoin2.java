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
public class KuCoin2 extends Exchange {

	public KuCoin2(Client client, Properties properties) {
		super(client, properties);
		setCondition("Token Listing");
	}

	public String request() {
		Map<String, String> headers = new Hashtable<String, String>();
		headers.put("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
		headers.put("Accept-Language", "en-GB,en;q=0.9,en-US;q=0.8,fr;q=0.7,ar;q=0.6,es;q=0.5,de;q=0.4");
		headers.put("Cache-Control", "max-age=0");
		headers.put("Connection", "keep-alive");
		headers.put("Upgrade-Insecure-Requests", "1");
		headers.put("User-Agent", Util.USER_AGENT);
		try {

			return this.getClient().doGet(this.getUrl(), "https://blog.kuCoin.com/", headers).getResponse();
		} catch (Exception e) {
			return null;
		}
	}

	public HashSet<Model> parse(String announcements) {

		Document doc = Jsoup.parse(announcements);
		HashSet<Model> models = new HashSet<Model>();
		Elements news = doc.select("[class*=main] div[class*=info]");
		for (Element e : news) {
			String code = e.select("a").attr("href");
			String title = e.select("a").text().trim();
			if (code != null && !code.equals("") && title != null && !title.equals(""))
				models.add(new Model("https://www.kucoin.com" + code, title));
		}
		return models;
	}

}
