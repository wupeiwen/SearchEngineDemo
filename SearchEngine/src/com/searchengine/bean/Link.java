package com.searchengine.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
/**
 * 链接实体类
 * @author BinG
 *
 */
@Entity
public class Link {
	
	private int id;//主键 自增
	private String url;//原始url地址
	private String link_md5;//urlMD5值
	private String date;//爬取日期
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getLink_md5() {
		return link_md5;
	}
	public void setLink_md5(String link_md5) {
		this.link_md5 = link_md5;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	
}
