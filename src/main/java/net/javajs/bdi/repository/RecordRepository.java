package net.javajs.bdi.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import net.javajs.bdi.collection.Record;

public interface RecordRepository extends MongoRepository<Record, String> {
	public List<Record> findAll();
}
