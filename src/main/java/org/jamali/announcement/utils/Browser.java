package org.jamali.announcement.utils;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
/**
 * selenium web browser for some cas
 * @author said
 *
 */
public class Browser {

	private static final Logger logger = LogManager.getLogger(Browser.class);

	private WebDriver driver;
	private FluentWait<WebDriver> fluentWait;

	public Browser() {
	}

	public Browser setUp(String browserName) {

		if (browserName.equalsIgnoreCase("chrome")) {
			ChromeOptions chromeOptions = new ChromeOptions();
			chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
			System.setProperty("webdriver.chrome.driver", "resources/bin/chromedriver");
			driver = new ChromeDriver(chromeOptions);
		}

		else if (browserName.equalsIgnoreCase("firefox")) {
			FirefoxOptions firefoxOptions = new FirefoxOptions();
			firefoxOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
			System.setProperty("webdriver.gecko.driver", "resources/bin/geckodriver");
			driver = new FirefoxDriver(firefoxOptions);
		}
		return this;

	}

	public Browser initFluentWait(int wait) {
		this.fluentWait = new FluentWait<>(driver).withTimeout(Duration.ofSeconds(wait))
				.pollingEvery(Duration.ofMillis(250)).ignoring(NoSuchElementException.class);
		return this;
	}

	public WebDriver getDriver() {
		return driver;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	public void tearDown() {
		this.driver.quit();
		this.driver = null;
		this.fluentWait = null;
	}

	public void refresh() {
		driver.navigate().refresh();
	}

	public boolean waitUntil(By by) {
		try {
			fluentWait.until(ExpectedConditions.visibilityOfElementLocated(by));
			return true;
		} catch (Exception e) {
			logger.error("Element " + by + " not found in the page");
			return false;
		}

	}

	public Browser chrome() {
		this.setUp("chrome").initFluentWait(60);
//		driver.manage().window().setSize(new Dimension(200, 600));
		return this;
	}

}
