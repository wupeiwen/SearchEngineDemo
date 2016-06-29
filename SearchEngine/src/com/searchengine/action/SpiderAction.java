package com.searchengine.action;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.opensymphony.xwork2.ActionSupport;
import com.searchengine.dao.LinkDao;
import com.searchengine.util.Spider;
@Controller("SpiderAction")
@Scope("prototype")
public class SpiderAction extends ActionSupport {
	private String link;
	private boolean isTrue = true;
	@Resource(name = "LinkDao")
	private LinkDao linkDao;
	private List<String> todo;
	private boolean cancel = false;
	HttpServletResponse response = ServletActionContext.getResponse();
	HttpServletRequest request = ServletActionContext.getRequest (); 
	public String spider() throws IOException{
		/*while(true){
			
		}*/
		//getAllLinks(link);
		//String s =linkDao.addLinks(link);
		//System.out.println(s);
		
		List<String> list = new ArrayList<String>();
		list.add(link);
		setTodo(list);
		begin();
		return SUCCESS;
	}
	//开始爬虫
	public void begin(){
		cancel = false;
		while(!todo.isEmpty() && !cancel){
			for(int i = 0;i < todo.size() && !cancel;i++){
				getAllLinks(todo.get(i));
			}
		}
	}
	//停止爬虫
	public void cancel(){
		cancel = true;
	}
	//获取所有链接
	public void getAllLinks(String url){
		Document doc;
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		try {
			doc = Jsoup.connect(url).get();
			Elements links = doc.select("a[href]");
			for(Element e : links){
				String temp = e.attr("abs:href");
				if(temp!="" && !"".equals(temp) && doc.title()!="" && !"".equals(doc.title()))
				todo.add(temp);
				String s = linkDao.addLinks(temp);
				PrintWriter pw = response.getWriter();
				pw.write(s+"："+temp);
			}
			return;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}
		
	}
	 public void clear(){
		 todo.clear();
	 }
	/* public static void main(String[] args) {
		SpiderAction spider = new SpiderAction();
		List<String> list = new ArrayList<String>();
		list.add("http://www.zhihu.com/people/xiao-fei-se-97");
		spider.setTodo(list);
		spider.begin();
	}	*/
	
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public LinkDao getLinkDao() {
		return linkDao;
	}
	public void setLinkDao(LinkDao linkDao) {
		this.linkDao = linkDao;
	}
	 public List<String> getTodo() {
			return todo;
		}
		public void setTodo(List<String> todo) {
			this.todo = todo;
		}
		public boolean isCancel() {
			return cancel;
		}
		public void setCancel(boolean cancel) {
			this.cancel = cancel;
		}
}
