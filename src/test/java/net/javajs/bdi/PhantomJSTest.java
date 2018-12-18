/*package net.javajs.bdi;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PhantomJSTest {

	private static final String PHANTOMJS_PATH = "src/main/resources/dri/phantomjs.exe";
	

	public static void main(String[] args) {
		PhantomJSTest test = new PhantomJSTest();
		test.checkSitePerformance("https://Xjsports.news.naver.com/basketball/news/index.nhn?type=latest");
	}

	public PhantomJSTest() {
	}
	
	*//**
	 * Initialize PhantomJSDriver.
	 *//*
	private PhantomJSDriver initDriver() {
			// set Capabilities
			DesiredCapabilities capabilities = DesiredCapabilities.phantomjs();
			capabilities.setJavascriptEnabled(true);
			
			System.setProperty("phantomjs.binary.path", PHANTOMJS_PATH);
			PhantomJSDriver driver = new PhantomJSDriver(capabilities);
			return driver;
	}

	*//**
	 * Check Performance.
	 *//*
	private void checkSitePerformance(String url) {

		PhantomJSDriver driver = null;

		try {
			driver = initDriver();
			driver.get(url);	// access to specified URL
			waitForLoad(driver);
			// Get values of Navigation Timing
			long startTime = (Long) driver.executeScript(
					"return window.performance.timing.navigationStart");
			long loadEndTime = (Long) driver.executeScript(
					"return window.performance.timing.loadEventEnd");
			long responseEndTime = (Long) driver.executeScript(
					"return window.performance.timing.responseEnd");

			System.out.format("Response Time : %d\n", responseEndTime - startTime);
			System.out.format("PageLoad Time : %d\n", loadEndTime - startTime);
		} finally {
			if (driver != null) {
				driver.quit();
			}
		}
	}

	*//**
	 * Wait for page load.
	 *//*
	void waitForLoad(PhantomJSDriver driver) {
	    ExpectedCondition<Boolean> pageLoadCondition = new
	        ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driver) {
	                return ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
				}
	        };
	    WebDriverWait wait = new WebDriverWait(driver, 60);
	    wait.until(pageLoadCondition);
	}
}
*/