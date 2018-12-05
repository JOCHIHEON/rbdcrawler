package net.javajs.bdi.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import net.javajs.bdi.collection.NaverKBLNews;

public interface NaverKBLNewsRepository extends MongoRepository<NaverKBLNews, String> {

	@Query("{aid:?0}")
	public List<NaverKBLNews> findByAid(String aid);
}
