package com.searchengine.dao;

import java.util.List;

public interface LinkDao{
	public String addLinks(String url);
	public List selectAllLinks();
}
