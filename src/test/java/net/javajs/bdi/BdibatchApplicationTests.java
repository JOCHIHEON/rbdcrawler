package net.javajs.bdi;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;
import net.javajs.bdi.collection.Record;
import net.javajs.bdi.repository.RecordRepository;
import net.javajs.bdi.service.KBLCrawlingService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class BdibatchApplicationTests {

	@Autowired
	private KBLCrawlingService kblcs;
	@Autowired
	private RecordRepository rredo;
	
	@Test
	public void contextLoads() throws IOException {
		List<Record> recordList = kblcs.crwaling("2018", "11");
		assertEquals(52, recordList.size());
		recordList = kblcs.crwaling("2018", "10");
		assertEquals(38, recordList.size());
		//rredo.saveAll(recordList);
		recordList = rredo.findAll();
		recordList.stream().forEach(r->{
			log.info("record=>{}",r);
		});
	}

}
