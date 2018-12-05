package net.javajs.bdi.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import net.javajs.bdi.collection.Player;
import net.javajs.bdi.collection.Team;
import net.javajs.bdi.collection.TeamDetail;
import net.javajs.bdi.repository.TeamRepository;
import net.javajs.bdi.service.KBLTeamCraw;

@Component
@EnableScheduling
@Slf4j
public class TeamTasks {

	@Autowired
	private KBLTeamCraw teams;
	@Autowired
	private TeamRepository teamRepo;

	private static final String[] TEAMCODES = { "16", "35", "55", "50", "30", "65", "60", "70", "06", "10" };
	private static int codeCnt = 0;

	// @Scheduled(initialDelay=0, fixedDelay=5)
	public void runTeamInfo() throws Exception {
		if (codeCnt == TEAMCODES.length) {
			System.exit(0);
		}

		Team team = teams.teamInfoCraw(TEAMCODES[codeCnt]);
		if (team.getTeam_name() != null) {
			log.info(team.toString());
			teamRepo.save(team);
			codeCnt++;
		}
	}

	private static int scode = 33;

	@Scheduled(cron ="0 30 21 * * *")
	public void runTeamDetail() throws Exception {
		if (scode == 27) {
			System.exit(0);
		}
		List<Team> teamList = teamRepo.findAll();
		for (Team team : teamList) {
			if (team.getTeamDetail() == null) {
				team.setTeamDetail(new ArrayList<>());
				log.info("객체별 한번 실행");
			}
		}

		List<Map<String, TeamDetail>> teamDeList = teams.teamDetailCraw(scode);

		if (teamList != null) {
			for (Map<String, TeamDetail> m : teamDeList) {
				String key = m.keySet().iterator().next();
				for (Team t : teamList) {
					if (t.getTeamDetail() != null) {
						if (t.getTeam_name().indexOf(key) != -1) {
							t.getTeamDetail().add(m.get(key));

						}
					}
				}
			}
			teamRepo.saveAll(teamList);
			log.info("teamList => {}", teamList);
			log.info("count => {}", teamRepo.count());
			scode = scode - 2;
		}
	}
}
