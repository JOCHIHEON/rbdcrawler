package net.javajs.bdi.repository.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import net.javajs.bdi.collection.Team;
import net.javajs.bdi.repository.TeamRepositoryMk;

@Repository
public class TeamRepositoryMKImpl implements TeamRepositoryMk {

	@Autowired
	private MongoTemplate mt;

	@Override
	public void ranksUpdate(List<Team> teams) {
		Criteria criteria = null;
		int i = 0;
		for(Team t : teams) {
			criteria = new Criteria();
			criteria.andOperator(Criteria.where("teamDetail.season").regex(t.getTeamDetail().get(0).getSeason(),"i").and("team_name").regex(t.getTeam_name()));
			Query query = new Query(criteria);
			Update update = new Update();
	        update.set("teamDetail.0.teamRank", t.getTeamDetail().get(0).getTeamRank());
			mt.updateFirst(query, update, Team.class);
			
		}
	}
}
