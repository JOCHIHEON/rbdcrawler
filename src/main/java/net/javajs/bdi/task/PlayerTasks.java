package net.javajs.bdi.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import net.javajs.bdi.collection.Player;
import net.javajs.bdi.collection.PlayerDetail;
import net.javajs.bdi.collection.Team;
import net.javajs.bdi.repository.TeamRepository;
import net.javajs.bdi.service.KBLPlayerCraw;

@Component
@EnableScheduling
@Slf4j
public class PlayerTasks {
	@Autowired
	private TeamRepository tRepo;

	@Autowired
	private KBLPlayerCraw players;

	private List<String> pcodes = null;
	private static int pcodeCnt = 0;

	// @Scheduled(initialDelay = 0, fixedDelay = 5)
	public void runPlayerInfo() throws IOException {
		if (pcodes == null) {
			pcodes = players.getPcode();
		}
		if (pcodes != null && pcodeCnt == pcodes.size()) {
			System.exit(0);
		}

		String[] ps = pcodes.get(pcodeCnt).split("-");
		Player plam = players.playerInfoCraw(ps[0]);

		if (plam != null) {
			plam.setPlayer_no(ps[0]);
			plam.set_id(new ObjectId());
			Team team = tRepo.findByteam_code(ps[1]);
			plam.setTeam_code(team.getTeam_code());
			if (team.getPlayer_info() == null) {
				team.setPlayer_info(new ArrayList<>());
			}
			team.getPlayer_info().add(plam);
			// log.info("pcnt, team => {}, {}",pcodeCnt, team);
			tRepo.save(team);
			pcodeCnt++;
			// log.info("total cnt=> {}, {}",pcodeCnt, tRepo.count());

		}
	}

	private List<String> tcodes = null;
	private static int tcodeCnt = 0;

	//@Scheduled(initialDelay = 0, fixedDelay = 500)
	public void runPlayerDetail() throws IOException {
		if (tcodes == null) {
			tcodes = new ArrayList<>();
			for (Team t : tRepo.finByCode()) {
				tcodes.add(t.getTeam_code());
			}
		}
		if (tcodeCnt == tcodes.size()) {
			System.exit(0);
		}

		List<Team> teams = new ArrayList<>();
		List<PlayerDetail> pDtails = players.runPlayerDetail(tcodes.get(tcodeCnt));

		Integer teamPlayers = tRepo.findByTeamPlayers(tcodes.get(tcodeCnt)).getPlayer_info().size();
		List<Player> player = new ArrayList<>();
		Team team = null;
		for (PlayerDetail detail : pDtails) {
			team = tRepo.findByPlayer(detail.getTeam_code(), detail.getPlayer_no());
			if (team == null || team.getPlayer_info().size() != 1) {
				continue;
			}
			team.getPlayer_info().get(0).setPlayerDetail(detail);
			player.add(team.getPlayer_info().get(0));
			
		}
		team = tRepo.findByTeamPlayers(tcodes.get(tcodeCnt));
		team.setPlayer_info(player);
		teams.add(team);
		tcodeCnt++;

		for(Team teasm : teams) {
			log.info(teasm.toString());
		}
		
		tRepo.saveAll(teams);
		
	}
}
