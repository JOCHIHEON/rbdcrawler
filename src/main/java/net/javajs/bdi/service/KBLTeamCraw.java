package net.javajs.bdi.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import net.javajs.bdi.collection.Team;
import net.javajs.bdi.collection.TeamDetail;

public interface KBLTeamCraw {

	public Team teamInfoCraw(String team_code)throws IOException;
	public List<Map<String, TeamDetail>> teamDetailCraw(Integer scode)throws IOException;
	
}
