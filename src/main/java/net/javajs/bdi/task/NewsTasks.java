package net.javajs.bdi.task;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import net.javajs.bdi.collection.NaverKBLNews;
import net.javajs.bdi.repository.NaverKBLNewsRepository;
import net.javajs.bdi.service.NaverKBLCraw;

@Component
@EnableScheduling
@Slf4j
public class NewsTasks {
	@Autowired
	private NaverKBLCraw ns;
	@Autowired
	private NaverKBLNewsRepository nRepo;
	
	// cron 표현식 설명
	// http://blog.naver.com/PostView.nhn?blogId=lovemema&logNo=140200056062
	@Scheduled(initialDelay=0, fixedDelay=1000 * 600)
	public void newsCraw() throws IOException {
		log.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		List<NaverKBLNews> newsList = ns.naverCraw();
		if(newsList.size()==0) {
			return;
		}
		nRepo.saveAll(newsList);
		log.info("db cnt, list cnt=> {} {}",nRepo.count(),newsList.size());
	}
}
