package net.javajs.bdi.task;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import net.javajs.bdi.collection.Record;
import net.javajs.bdi.repository.RecordRepository;
import net.javajs.bdi.service.KBLCrawlingService;

@Component
@EnableScheduling
@Slf4j
public class JobTasks {
	private static final AtomicInteger MONTH;
	private static final AtomicInteger JOB_COUNTER;
	static {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int month = cal.get(Calendar.MONTH);
		log.info("start month=>{}",month);
		MONTH = new AtomicInteger(month+1);
		JOB_COUNTER = new AtomicInteger();
	}

	@Autowired
	private KBLCrawlingService kblcs;
	@Autowired
	private RecordRepository rrepo;
	
	private static Integer startYear = 2018;
	private int htmlCnt = 0;

	@Scheduled(initialDelay = 0, fixedDelay = 5)
	public void runJob() throws Exception {
		if(startYear==2008) {
			System.exit(0);
		}
		if(MONTH.get()==0) {
			MONTH.set(12);
			startYear--;
		}
		List<Record> recordList = kblcs.crwaling(startYear.toString(), MONTH.toString());
		if(recordList!=null) {
			log.info("{}번째 잡", JOB_COUNTER.incrementAndGet());
			htmlCnt += recordList.size();
			rrepo.saveAll(recordList);
			log.info("total cnt=> {}, {}",htmlCnt, rrepo.count());
		}
		MONTH.decrementAndGet();
	}

	@Bean
	public TaskScheduler taskScheduler() {
		ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
		taskScheduler.setPoolSize(10);
		return taskScheduler;
	}
}
