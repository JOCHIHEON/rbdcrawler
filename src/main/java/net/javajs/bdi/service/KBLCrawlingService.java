package net.javajs.bdi.service;

import java.io.IOException;
import java.util.List;

import net.javajs.bdi.collection.Record;

public interface KBLCrawlingService {
	public List<Record> crwaling(String year, String month)throws IOException;
}
