package net.javajs.bdi.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import net.javajs.bdi.collection.Record;

public interface RecordRepository extends MongoRepository<Record, String> {
	
	@Query(value="{$and : [ {'date' : ?0}, {'homeName' : ?1}, {'awayName' : ?2} ]}")
	public List<Record> findByRecord(String date, String home, String away);
	
	@Query(value="{ 'date' : { $gt: '20181012' } }") //2018 - 2019 시즌
	public List<Record> findByDate();
}
