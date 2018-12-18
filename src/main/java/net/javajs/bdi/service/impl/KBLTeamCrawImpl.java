package net.javajs.bdi.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.javajs.bdi.collection.Team;
import net.javajs.bdi.collection.TeamDetail;
import net.javajs.bdi.service.KBLTeamCraw;

@Service
@Slf4j
public class KBLTeamCrawImpl implements KBLTeamCraw {

	@Override
	public Team teamInfoCraw(String team_code) throws IOException {
		String url = "https://www.kbl.or.kr/teams/team_info.asp?tcode=";
		String tNameEle = "div>h5";
		String tInfoEles = "dd>dl>dd";
		Document doc = Jsoup.connect(url + team_code).get();
		Element tName = doc.selectFirst(tNameEle);
		Elements tInfos = doc.select(tInfoEles);

		Team team = new Team();
		List<String> arr = new ArrayList<>();

		for (Element tInfo : tInfos) {
			arr.add(tInfo.text());
		}
		team.setTeam_code(team_code);
		team.setTeam_name(tName.text());
		team.setTeam_owner(arr.get(0));
		team.setTeam_reowner(arr.get(1));
		team.setTeam_leader(arr.get(2));
		team.setTeam_director(arr.get(3));
		team.setTeam_coach(arr.get(4));
		team.setTeam_address(arr.get(5));
		team.setTeam_company(arr.get(6));

		return team;
	}

	@Override
	public List<Map<String, TeamDetail>> teamDetailCraw(Integer scode) throws IOException {
		String url = "https://www.kbl.or.kr/stats/part_team_rank.asp?gpart=1&scode=";
		String season = "select[name=\"scode\"]>option[selected]";
		String tBodys = "table>tbody>tr";
		Document doc = Jsoup.connect(url + scode).get();
		Elements tBodyEles = doc.select(tBodys);
		Element seasonEle = doc.selectFirst(season);

		List<Map<String, TeamDetail>> teamList = new ArrayList<>();

		for (Element tBody : tBodyEles) {
			String tr = tBody.text().substring(tBody.text().indexOf(" ") + 1);
			List<Double> trCut = new ArrayList<>();
			String tname = "";
			for (String s : tr.split(" ")) {
				try {
					trCut.add(Double.parseDouble(s));
				} catch (NumberFormatException e) {
					if(s.substring(2).equals("동부")) {
						tname = "DB";
						continue;
					}
					tname = s.substring(2);
				}
			}
		
			Map<String, TeamDetail> team = new HashMap<>();
			if (!tname.equals("")) {
				TeamDetail teamDe = new TeamDetail();
				
				teamDe.setSeason(seasonEle.text());
				teamDe.setTeamde_pts(trCut.get(1));
				teamDe.setTeamde_reb(trCut.get(2));
				teamDe.setTeamde_assist(trCut.get(3));
				teamDe.setTeamde_stl(trCut.get(4));
				teamDe.setTeamde_blk(trCut.get(5));
				teamDe.setTeamde_fg(trCut.get(6));
				teamDe.setTeamde_fgp(trCut.get(7));
				teamDe.setTeamde_threept(trCut.get(8));
				teamDe.setTeamde_threeptp(trCut.get(9));
				teamDe.setTeamde_ft(trCut.get(10));
				teamDe.setTeamde_ftp(trCut.get(11));
				team.put(tname, teamDe);
			}else {
				return null;
			}
			teamList.add(team);
		}
		return teamList;
	}
}
