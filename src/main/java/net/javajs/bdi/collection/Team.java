package net.javajs.bdi.collection;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document("team_info")
public class Team {
	private ObjectId _id;
	private String team_code;
	private String team_name;
	private String team_owner;
	private String team_reowner;
	private String team_leader;
	private String team_director;
	private String team_coach;
	private String team_address;
	private String team_company;
	private List<TeamDetail> teamDetail;
	private List<Player> player_info;
}