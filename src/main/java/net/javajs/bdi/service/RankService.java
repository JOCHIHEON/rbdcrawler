package net.javajs.bdi.service;

import java.util.List;

import net.javajs.bdi.collection.Record;
import net.javajs.bdi.collection.Team;

public interface RankService {

	public List<Team> rankSave(List<Record> recordList);
}
