package net.javajs.bdi.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.javajs.bdi.collection.NaverKBLNews;
import net.javajs.bdi.repository.NaverKBLNewsRepository;
import net.javajs.bdi.service.NaverKBLCraw;

@Service
@Slf4j
public class NaverKBLCrawImpl implements NaverKBLCraw {

	@Autowired
	private NaverKBLNewsRepository nRepo;
	
	private static final String HEADERVALUE = "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:47.0) Gecko/20100101 Firefox/47.0";

	private WebDriver driver;
	private ChromeOptions cOpt = new ChromeOptions();
	private WebDriverWait wait;

	@Override
	public List<NaverKBLNews> naverCraw() throws IOException {
		startUp();
		driver.get("https://sports.news.naver.com/basketball/news/index.nhn?type=latest");

		wait = new WebDriverWait(driver, 15);
		WebElement kblBtn = driver.findElement(By.xpath("//li[@data-id=\"kbl\"]"));
		kblBtn.click();

		WebElement te = driver.findElement(By.id("_newsList"));
		te = wait.until(ExpectedConditions.elementToBeClickable((By.id("_newsList"))));

		List<WebElement> wlist = te.findElements(By.className("text"));
		List<NaverKBLNews> newsList = new ArrayList<>();
		try {
			for (WebElement w : wlist) {
				NaverKBLNews kblnews = new NaverKBLNews();
				
				String uri = w.findElement(By.tagName("a")).getAttribute("href");
				String title = w.findElement(By.tagName("span")).getText();
				String text = w.findElement(By.tagName("p")).getText();
				String company = w.findElement(By.className("press")).getText();
				String date = w.findElement(By.className("time")).getText();
				String aid = uri.substring(uri.indexOf("aid=")).replace("aid=", "");
				if(aid.indexOf("&") != -1) {
					aid = aid.substring(0,aid.indexOf("&"));
				}
				String oid = uri.substring(uri.indexOf("oid=")).replace("oid=", "");
				if(oid.indexOf("&") != -1) {
					oid = oid.substring(0,oid.indexOf("&"));
				}
				
				if (nRepo.findByAid(aid).size() != 0) {
					continue;
				}
				
				kblnews.setUri(uri);
				kblnews.setTitle(title);
				kblnews.setText(text);
				kblnews.setCompany(company);
				kblnews.setDate(date);
				kblnews.setOid(oid);
				kblnews.setAid(aid);
				newsList.add(kblnews);
			}
		}catch (StaleElementReferenceException e) {
			shutDown();
			log.info("요소가 페이지 문서에 첨부되지 않았습니다.");
			log.info("org.openqa.selenium.StaleElementReferenceException: stale element reference: element is not attached to the page document");
		}
		log.info("list size and content => {}, {}",newsList.size(),newsList.toString());
		shutDown();
		return newsList;
	}

	private void startUp() {
		String pathToChrome = "C:\\kblcrawl\\chromedriver.exe";
		//String pathToChrome = "/home/ec2-user/chromedriver";
		System.setProperty("webdriver.chrome.driver", pathToChrome);
		cOpt.addArguments("user-agent=" + HEADERVALUE);
		cOpt.addArguments("headless");
		driver = new ChromeDriver(cOpt);
	}

	private void shutDown() {
		driver.quit();
	}
}