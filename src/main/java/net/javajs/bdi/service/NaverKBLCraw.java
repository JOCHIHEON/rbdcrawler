package net.javajs.bdi.service;

import java.io.IOException;
import java.util.List;

import net.javajs.bdi.collection.NaverKBLNews;

public interface NaverKBLCraw {
	public List<NaverKBLNews> naverCraw() throws IOException;
}
