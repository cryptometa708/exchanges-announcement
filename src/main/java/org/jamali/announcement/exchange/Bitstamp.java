package org.jamali.announcement.exchange;

import java.util.HashSet;
import java.util.Properties;
import java.util.stream.Collectors;

import org.jamali.announcement.utils.Browser;
import org.jamali.announcement.utils.Client;
import org.jamali.announcement.utils.Model;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
/**
 * 
 * @author Ibrahim Jamali
 *
 */
public class Bitstamp extends Exchange {

	private Browser browser;

	public Bitstamp(Client client, Properties properties) {
		super(client, properties);
		this.setCondition("post__title-link");
		this.browser = new Browser().chrome();
	}

	public void setBrowser(Browser browser) {
		if (browser != null)
			this.browser = browser;
		else
			throw new RuntimeException("Browser must be not null");

	}

	public Browser getBrowser() {
		return browser;
	}

	public HashSet<Model> parse(String announcements) {
		WebDriver driver = this.browser.getDriver();
		driver.navigate().to(this.getUrl());
		HashSet<Model> data = new HashSet<Model>();
		By article = By.cssSelector("a.post__title-link");
		if (this.browser.waitUntil(article)) {
			HashSet<Model> news = driver.findElements(article).parallelStream()
					.map(element -> new Model(element.getAttribute("href"), element.getText()))
					.collect(Collectors.toCollection(() -> data));
			if (news == null || news.size() == 0) {
				this.browser.tearDown();
				this.browser = new Browser().chrome();
			}

		}
		return data;
	}

	@Override
	public String request() {
		return "post__title-link";
	}

}
