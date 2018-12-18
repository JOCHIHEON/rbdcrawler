package net.javajs.bdi.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import net.javajs.bdi.collection.Team;

public interface TeamRepository extends MongoRepository<Team, String>{
	
	//@Query(value = "{team_name: ?0}", fields="{team_code:1, _id:1, player_info:1}")
	@Query(value = "{'team_name' : {$regex : ?0, $options: 'i'}}")
	public Team findByteam_code(String team_code);
	
	@Query(value = "{ 'team_code' : ?0, 'player_info' : { '$elemMatch' : { 'player_no' : ?1 } } }",fields = " { 'player_info' : { '$elemMatch' : { 'player_no' : ?1 } } }" )
	public Team findByPlayer(String team_code, String player_no);

	@Query(value = "{ 'player_info' : { '$elemMatch' : {_id : ?0} } }",fields= "{ 'player_info' : { '$elemMatch' : {_id : ?0} } }")
	public Team findByObjId(String objid);

	@Query(value="{}",fields = "{team_code : 1}")
	public List<Team> finByCode();
	
	@Query(value="{'team_code' : ?0}")
	public Team findByTeamPlayers(String team_code);
	
	@Query(value = "{'team_name' : {$regex : ?0, $options: 'i'}}")
	public Team findByTeams(String team_name);
	
	@Query(value = "{}", fields="{ team_code : 1, team_name : 1, teamDetail : 1 }")
	public List<Team> findByTeamList();
}

/*{ 'team_code' : ?0, 'player_info' : { '$elemMatch' : { 'player_name' : ?1 } } },  { 'player_info' : { '$elemMatch' : { 'player_name' : ?1 } } }
    */
