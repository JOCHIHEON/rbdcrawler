package net.javajs.bdi.collection;

import org.bson.types.ObjectId;

import lombok.Data;

@Data
//@Document("player_info")
public class Player {
	private ObjectId _id;
	private String player_no;
	private String team_code;
	private String player_name;
	private String player_birth;
	private String player_height;
	private String player_position;
	private Integer player_lookupcnt;
	private String player_background;
	private Integer player_number;
	private PlayerDetail playerDetail;
}
