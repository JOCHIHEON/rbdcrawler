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
import net.javajs.bdi.collection.Player;
import net.javajs.bdi.collection.PlayerDetail;
import net.javajs.bdi.service.KBLPlayerCraw;

@Service
@Slf4j
public class KBLPlayerCrawImpl implements KBLPlayerCraw {

	@Override
	public Player playerInfoCraw(String pcode) throws IOException {
		String urlPer = "https://www.kbl.or.kr/players/player_info.asp?pcode=";
		String urlPost = "&flag1=1&flag2=0&tcode=&sname=";
		String dl = "dd.pinfotxt_2015>dl>dd";
		Document doc = Jsoup.connect(urlPer + pcode + urlPost).get();
		Elements dlEles = doc.select(dl);

		Player player = new Player();
		List<String> infos = new ArrayList<>();
		for (Element e : dlEles) {
			String v = e.text();
			if (v.indexOf("/") != -1) {
				for (String s : v.split("/")) {
					infos.add(s.replace(" ", ""));
				}
				continue;
			}
			infos.add(v);
		}
		player.setPlayer_name(infos.get(0));
		player.setPlayer_birth(infos.get(1));
		player.setPlayer_height(infos.get(2));
		player.setPlayer_position(infos.get(4));
		player.setPlayer_number(Integer.parseInt(infos.get(5)));
		player.setPlayer_background(infos.get(6));

		// log.info(player.toString());

		return player;
	}

	@Override
	public List<String> getPcode(int flag2) throws IOException {
		String url = "https://www.kbl.or.kr/players/player_list.asp?flag2="+flag2;
		String divs = "div#subcontents>div.seqlist";
		String teamName = "ul>li";
		String pcodes = "a[href]";
		Document doc = Jsoup.connect(url).get();
		Elements divEles = doc.select(divs);

		List<String> players = new ArrayList<>();
		for (Element div : divEles) {
			for (Element li : div.select(teamName)) {
				String tname = li.text().substring(li.text().indexOf("[") + 1, li.text().indexOf("]"));
				tname = tname.substring(0, 2) + " " + tname.substring(2);
				Element a = li.selectFirst(pcodes);
				String href = a.attr("href").substring(a.attr("href").indexOf("?") + 1);
				String pcode = href.substring(href.indexOf("pcode="), href.indexOf("&"));
				pcode = pcode.replace("pcode=", "");
				players.add(pcode + "-" + tname);
			}
		}
		log.info("size => {}", players.size());
		return players;
	}

	@Override
	public List<PlayerDetail> runPlayerDetail(String tcode) throws IOException {
		String url = "https://www.kbl.or.kr/teams/team_record.asp?tcode=";
		String tables = "div#subcontents>table";
		Document doc = Jsoup.connect(url + tcode).get();
		Elements tabEles = doc.select(tables);
		List<PlayerDetail> details = new ArrayList<>();
		for (Element table : tabEles) {
			String caption = table.select("caption").text();
			String[] ths = table.select("thead>tr>th").text().replace("Off Def", "").replace("REBOUNDS", "Off Def")
					.split(" ");
			Elements trs = table.select("tbody>tr");

			for (Element tr : trs) {
				PlayerDetail pd =null;
				if (caption.indexOf("1") != -1) {
					pd = new PlayerDetail();
				}
				Element a = tr.selectFirst("td>a[href]");
				String pcode = a.attr("href");
				pcode = pcode.substring(pcode.indexOf("pcode=")).replace("pcode=", "");
				if (pcode.indexOf("&") != -1) {
					pcode = pcode.substring(0, pcode.indexOf("&"));
				}

				Map<String, String> m = new HashMap<>();
				for (int i = 2; i < ths.length; i++) {
					m.put(ths[i], tr.child(i).text());
				
				}
				if (caption.indexOf("1") != -1) {
					pd.setTeam_code(tcode);
					pd.setPlayer_no(pcode);
					pd.setPlayer_game(Double.parseDouble(m.get(ths[2])));
					pd.setPlayer_min(m.get(ths[3]));
					pd.setPlayer_2p(Double.parseDouble(m.get(ths[4])));
					pd.setPlayer_2pa(Double.parseDouble(m.get(ths[5])));
					pd.setPlayer_3p(Double.parseDouble(m.get(ths[6])));
					pd.setPlayer_3pa(Double.parseDouble(m.get(ths[7])));
					pd.setPlayer_fgp(Double.parseDouble(m.get(ths[9])));
					pd.setPlayer_ft(Double.parseDouble(m.get(ths[10])));
					pd.setPlayer_fta(Double.parseDouble(m.get(ths[11])));
					details.add(pd);
				} else if (caption.indexOf("2") != -1) {
					for(PlayerDetail detail : details) {
						if(detail.getPlayer_no().equals(pcode)) {
							detail.setPlayer_off(Double.parseDouble(m.get(ths[2])));
							detail.setPlayer_def(Double.parseDouble(m.get(ths[3])));
							detail.setPlayer_rpg(Double.parseDouble(m.get(ths[4])));
							detail.setPlayer_ast(Double.parseDouble(m.get(ths[5])));
							detail.setPlayer_apg(Double.parseDouble(m.get(ths[6])));
							detail.setPlayer_wft(Double.parseDouble(m.get(ths[7])));
							detail.setPlayer_woft(Double.parseDouble(m.get(ths[8])));
						}
					}
				} else if (caption.indexOf("3") != -1) {
					for(PlayerDetail detail : details) {
						if(detail.getPlayer_no().equals(pcode)) {
							detail.setPlayer_stl(Double.parseDouble(m.get(ths[2])));
							detail.setPlayer_bs(Double.parseDouble(m.get(ths[3])));
							detail.setPlayer_gd(Double.parseDouble(m.get(ths[4])));
							detail.setPlayer_to(Double.parseDouble(m.get(ths[5])));
							detail.setPlayer_dk(Double.parseDouble(m.get(ths[6])));
							detail.setPlayer_dka(Double.parseDouble(m.get(ths[7])));
							detail.setPlayer_pts(Double.parseDouble(m.get(ths[8])));
							detail.setPlayer_ppg(Double.parseDouble(m.get(ths[9])));
						}
					}
				}
			}
		}
		return details;
	}
}

