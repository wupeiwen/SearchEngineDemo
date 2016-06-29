package com.searchengine.dao.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;


import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;


import com.searchengine.bean.Link;
import com.searchengine.dao.LinkDao;
import com.searchengine.util.MD5Util;
@Component("LinkDao")
public class LinkDaoImpl implements LinkDao{
	private Link link;
	@Resource
	private HibernateTemplate ht;
	public String addLinks(String url) {
		link = new Link();
		link.setUrl(url);
		Date now = new java.util.Date();
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = dateformat.format(now);
		link.setDate(date);
		String link_md5;
		link_md5 = MD5Util.MD5(url);
		link.setLink_md5(link_md5);
		int i = 0;
		String sql = "from Link l where l.link_md5 = \'"+link_md5+"\'";
		List list = ht.find(sql);
			if(list.isEmpty()){
				ht.save(link);
				return url+"已插入！";
			} else{
				return url+"已访问！";
			}
		
	
	}
	public List selectAllLinks() {
		// TODO Auto-generated method stub
		String sql = "from Link l";
		List<Link> list = new ArrayList<Link>();
				list=ht.find(sql);
		return list;
	}
}
