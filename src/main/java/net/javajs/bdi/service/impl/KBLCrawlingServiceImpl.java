package net.javajs.bdi.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.javajs.bdi.collection.Record;
import net.javajs.bdi.service.KBLCrawlingService;

@Service
@Slf4j
public class KBLCrawlingServiceImpl implements KBLCrawlingService {
	/*
	 *  crawl.target.url=https://sports.news.naver.com/basketball/schedule/index.nhn?category=kbl
		crawl.all.query.selector=div.sch_tb:not(.nogame),div.sch_tb2:not(.nogame2)
		crawl.date.query.selector=span.td_date>strong
		crawl.home.query.selector=span.team_lft
		crawl.away.query.selector=span.team_rgt
		crwal.score.query.selector=strong.td_score
	 */
	@Value("${crawl.target.url}")
	private String targetUrl;
	@Value("${crawl.all.query.selector}")
	private String allQuery;
	@Value("${crawl.date.query.selector}")
	private String dateQuery;
	@Value("${crawl.home.query.selector}")
	private String homeQuery;
	@Value("${crawl.away.query.selector}")
	private String awayQuery;
	@Value("${crwal.score.query.selector}")
	private String scoreQuery;
	
	@Override
	public List<Record> crwaling(String year, String month) throws IOException {
		Document doc = Jsoup.connect(targetUrl+"&year=" + year + "&month="+month).get();
		log.info(targetUrl+"&year=" + year + "&month="+month);
		Elements tagVal = doc.select(allQuery);
		List<Record> recordList = new ArrayList<Record>();
		
		for(Element el:tagVal) {
			Elements gameDate = el.select(dateQuery);
			String m = gameDate.text().split("\\.")[0];
			if(!month.equals(m)) {
				return null;
			}
			if(m.length()<2) {
				m = "0" + m;
			}
			String d = gameDate.text().split("\\.")[1];
			if(d.length()<2) {
				d = "0" + d;
			}
			Elements teamLft = el.select(homeQuery);
			Elements teamRgt = el.select(awayQuery);
			Elements score = el.select(scoreQuery);
			for(int i=0;i<teamLft.size();i++) {
				String[] scores = score.get(i).text().split(":");
				try {
					Integer.parseInt(scores[0]);
				}catch(NumberFormatException nfe) {
					continue;
				}
				Record record = new Record();
				record.setDate(year+m+ d);
				record.setHomeName(teamLft.get(i).text());
				record.setHomeScore(Integer.parseInt(scores[0]));
				record.setAwayName(teamRgt.get(i).text());
				record.setAwayScore(Integer.parseInt(scores[1]));
				record.setIsTransfer(false);
				recordList.add(record);
			}
		}
		return recordList;
	}

}
