package net.javajs.bdi.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import net.javajs.bdi.collection.Record;
import net.javajs.bdi.collection.Team;
import net.javajs.bdi.repository.RecordRepository;
import net.javajs.bdi.repository.TeamRepositoryMk;
import net.javajs.bdi.service.KBLCrawlingService;
import net.javajs.bdi.service.RankService;

@Component
@EnableScheduling
@Slf4j
public class JobTasks {
	
	/*private static final AtomicInteger MONTH;
	private static final AtomicInteger JOB_COUNTER;
	static {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int month = cal.get(Calendar.MONTH);
		log.info("start month=>{}",month);
		MONTH = new AtomicInteger(month+1);
		JOB_COUNTER = new AtomicInteger();
	}*/

	@Autowired
	private KBLCrawlingService kblcs;
	@Autowired
	private RecordRepository rrepo;
	
	@Autowired
	private RankService ranks;
	
	@Autowired
	private TeamRepositoryMk teamrepoMk;
	/*
	private static Integer startYear = 2018;
	private int htmlCnt = 0;

	@Scheduled(initialDelay = 0, fixedDelay = 5) //cron=" 0 30 21 * * *"
	public void runJob() throws Exception {
		Date date = new Date();
		SimpleDateFormat month = new SimpleDateFormat("MM");
		SimpleDateFormat year = new SimpleDateFormat("YYYY");
		log.info(month.format(date)+""+year.format(date));
		System.exit(0);
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
	}*/

	@Scheduled(cron=" 0 30 12 * * *") //utc 시간 기준
	public void runScoreJob() throws Exception {
		Date date = new Date();
		SimpleDateFormat month = new SimpleDateFormat("MM");
		SimpleDateFormat year = new SimpleDateFormat("YYYY");
		List<Record> recordList = kblcs.crwaling(year.format(date), month.format(date));
		if(recordList!=null) {
			Iterator<Record> r = recordList.iterator();
			while(r.hasNext()) {
				Record record = r.next();
				if(rrepo.findByRecord(record.getDate(), record.getHomeName(), record.getAwayName()).size() != 0 ) {
					r.remove();
					continue;
				}//중복 방지
			}
			log.info("update score => {}",recordList.size());
			if(recordList.size()>0) {
				rrepo.saveAll(recordList);
				//순위 저장
				List<Team> rank = ranks.rankSave(recordList);
				teamrepoMk.ranksUpdate(rank);
			}
		}
	}
	/*@Scheduled(initialDelay = 0, fixedDelay = 5000)
	public void test() {
		List<Record> recordList = rrepo.findByDate();
		log.info("list => {} {}", recordList.size(),recordList);
		List<Team> rank = ranks.rankSave(recordList);
		for(Team t : rank) {
			log.info("t = {}",t.getTeamDetail().get(0).getTeamRank());
		}
		teamrepoMk.ranksUpdate(rank);
		System.exit(0);
	}*/
	@Bean
	public TaskScheduler taskScheduler() {
		ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
		taskScheduler.setPoolSize(10);
		return taskScheduler;
	}
}
