package net.javajs.bdi.service;

import java.io.IOException;
import java.util.List;

import net.javajs.bdi.collection.Player;
import net.javajs.bdi.collection.PlayerDetail;

public interface KBLPlayerCraw {
	public List<String> getPcode() throws IOException;
	public  Player playerInfoCraw(String pcode) throws IOException;
	
	public List<PlayerDetail> runPlayerDetail(String tcode) throws IOException;
}
