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
public class Ftx extends Exchange {

	private Browser browser;

	public Ftx(Client client, Properties properties) {
		super(client, properties);
		setCondition("sections-list");
	}

	public void setBrowser(Browser browser) {
		this.browser = browser;
	}

	public Browser getBrowser() {
		return browser;
	}

	public HashSet<Model> parse(String announcements) {
		WebDriver driver = this.browser.getDriver();
		driver.navigate().to(this.getUrl());
		HashSet<Model> data = new HashSet<Model>();
		By article = By.className("sections-list");
		if (this.browser.waitUntil(article)) {
			HashSet<Model> news = driver.findElements(article).parallelStream()
					.map(element -> new Model(element.getAttribute("href"), element.getAttribute("innerHTML").trim()))
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
		return "sections-list";
	}

	@Override
	public void run() {
		this.browser = new Browser().chrome();
		super.run();
		this.browser.tearDown();
	}

}
