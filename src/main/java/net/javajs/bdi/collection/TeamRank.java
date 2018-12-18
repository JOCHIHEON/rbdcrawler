package net.javajs.bdi.collection;

import lombok.Data;

@Data
public class TeamRank implements Comparable<TeamRank> {
	private Integer rank;
	private String teamName;
	private Integer win;
	private Integer lose;
	private Double shift;
	
	@Override
	public int compareTo(TeamRank o) {
		return shift.compareTo(o.getShift());
	}
}
