package net.javajs.bdi.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.javajs.bdi.collection.Record;
import net.javajs.bdi.collection.Team;
import net.javajs.bdi.collection.TeamRank;
import net.javajs.bdi.repository.TeamRepository;
import net.javajs.bdi.service.RankService;

@Slf4j
@Service
public class RankServiceImpl implements RankService {

	@Autowired
	private TeamRepository teamRepo;
	
	@Override
	public List<Team> rankSave(List<Record> recordList) {
		List<TeamRank> ranks = new ArrayList<>();
		List<Team> teams = teamRepo.findByTeamList();
		for (Team team : teams) {
			TeamRank rank  = new TeamRank();
			rank.setTeamName(team.getTeam_name());
			for (Record record : recordList) {
				if (team.getTeam_name().indexOf(record.getHomeName().substring(2)) != -1) {
					result(rank,record.getHomeScore(),record.getAwayScore());
				}else if(team.getTeam_name().indexOf(record.getAwayName().substring(2)) != -1) {
					result(rank,record.getAwayScore(),record.getHomeScore());
				}
			}
			double shift = (double) rank.getWin() / (rank.getWin()+rank.getLose());
			rank.setShift(shift);
			ranks.add(rank);
		}
		Collections.sort(ranks);
		for(int i=0; i<ranks.size();i++) {
			ranks.get(i).setRank(ranks.size()-i);
		}
		for(Team team : teams) {
			for(TeamRank rank : ranks) {
				if(team.getTeam_name().indexOf(rank.getTeamName()) != -1) {
					team.getTeamDetail().get(0).setTeamRank(rank);
					break;
				}
			}
		}
		return teams;
	}
	private static void result(TeamRank rank, int score1, int score2) {
		if(score1>score2) {
			rank.setWin(rank.getWin()==null?1:rank.getWin()+1);
		}else {
			rank.setLose(rank.getLose()==null?1:rank.getLose()+1);
		}
	}
}
