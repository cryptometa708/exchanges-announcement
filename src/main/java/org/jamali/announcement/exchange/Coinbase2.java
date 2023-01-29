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
public class Coinbase2 extends Exchange {

	private Browser browser;

	public Coinbase2(Client client, Properties properties) {
		super(client, properties);
		setCondition("tweet");
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

		By article = By.cssSelector("[data-testid=tweet] [lang=en]");
		if (this.browser.waitUntil(article)) {
			HashSet<Model> news = driver.findElements(article).parallelStream()
					.map(e -> new Model(e.getAttribute("id"), e.getText()))
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
		return "tweet";
	}

}
