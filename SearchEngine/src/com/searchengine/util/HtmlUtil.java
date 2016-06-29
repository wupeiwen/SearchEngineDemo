package com.searchengine.util;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.orm.hibernate3.HibernateTemplate;

import sun.jdbc.odbc.ee.DataSource;

import com.searchengine.bean.Link;
import com.searchengine.dao.LinkDao;
import com.searchengine.dao.impl.LinkDaoImpl;

public class HtmlUtil {
	private Connection conn = null;//数据库的链接

	private Statement stat = null;//数据库的语句

	private ResultSet rs = null;//数据库返回的结果集
	Spider spider = new Spider();
	public static String JsoupHtml(String urlStr) {
		URL url;
		String temp;
		String charset;
		if(urlStr==""||"".equals(urlStr)||urlStr == null){
			urlStr = "http://www.baidu.com/";
		}
		final StringBuffer sb = new StringBuffer();
		try {
			url = new URL(urlStr);
			System.out.println();
			charset = Decoder.parseByResHeader(urlStr);
			final BufferedReader in = new BufferedReader(new InputStreamReader(
					url.openStream(), charset));
			while ((temp = in.readLine()) != null) {
				sb.append(temp);
			}
			in.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			Saver.LOGWriter(urlStr, "MalformedURLException");
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			Saver.LOGWriter(urlStr, "IOException");
		}
		return sb.toString();
	}

	/**
	 * 使用正则表达式从htmlContent中解析出网页<title></title>标签对中的内容
	 * 
	 * @param htmlContent
	 *            网页的内容
	 * @return 网页的标题
	 */
	public static String getTitle(final String htmlCotent) {
		String regex_title;
		String title = "网页title为空";
		regex_title = "<[Tt][Ii][Tt][Ll][Ee]>(.*?)</[Tt][Ii][Tt][Ll][Ee]>";
		final Pattern pat = Pattern.compile(regex_title, Pattern.CANON_EQ);
		final Matcher mat = pat.matcher(htmlCotent);
		while (mat.find()) {
			title = mat.group(1);
		}
		return outTitleTag(title);
	}

	/**
	 * 使用正则表达式从htmlContent中解析出网页<meta></meta>标签对中的keywords的内容
	 * 
	 * @param htmlContent
	 *            网页内容
	 * @return 网页中的关键字
	 */
	public static String getKeywords(final String htmlContent) {
		String keywords = "null";
		
		String regex_keywords = "<meta name=[\"]{0,1}[Kk]eywords[\"]{0,1} content=[\"]{0,1}(.*?)[ ]{0,1}[\"]{0,1}[ ]{0,1}[/]{0,1}[ ]{0,1}>";

		Pattern p_keywords = Pattern.compile(regex_keywords);
		Matcher m_keywords = p_keywords.matcher(htmlContent);
		while (m_keywords.find()) {
			keywords = m_keywords.group(1);
		}
		return keywords;
	}

	/**
	 * 使用正则表达式从htmlContent中解析出网页<meta></meta>标签对中的对网页描述的内容
	 * 
	 * @param htmlContent
	 *            网页的内容
	 * @return 网页中对网页进行描述的内容
	 */
	public static String getDes(final String htmlContent) {
		String des = null;
		
		String regex_des = "<meta name=[\"]{0,1}[Dd]escription[\"]{0,1} content=[\"]{0,1}(.*?)[ ]{0,1}[\"]{0,1}[ ]{0,1}[/]{0,1}[ ]{0,1}>";
		
		Pattern p_des = Pattern.compile(regex_des, Pattern.DOTALL);
		Matcher m_des = p_des.matcher(htmlContent);
		while (m_des.find()) {
			des = m_des.group(1);
		}
		if (des == null) {
			des = HtmlUtil.getContent(htmlContent);
		}
		return des;
	}

	/**
	 * 使用正则表达式从htmlContent中解析出网页<body></body标签对中的有用内容
	 * 
	 * @param htmlContent
	 *            网页内容
	 * @return 网页body体中的有用内容
	 */
	public static String getContent(final String htmlContent) {
		String regex;
		String body = "";
		final StringBuffer sb = new StringBuffer();
		
		regex = "<body.*?</body>";

		final Pattern pat = Pattern.compile(regex, Pattern.DOTALL);
		final Matcher mat = pat.matcher(htmlContent);
		while (mat.find()) {
			sb.append(mat.group());
		}

		body = sb.toString();
		return outTag(body);
	}

	/**
	 * 去掉<title></title>中的无用的字符信息及不满足windows文件命名的字符信息
	 * 
	 * @param title
	 *            <title></title>标签对中内容
	 * @return 符合要求的网页标题
	 */
	public static String outTitleTag(final String title) {
		return title.replaceAll("\\s|\\|\\/|\\｜|:|\\*|\\?", "");
	}

	/**
	 * 去掉<body></body>中的无用的字符信息
	 * 
	 * @param title
	 *            <body></body>标签对中内容
	 * @return 符合要求的网页内容
	 */
	public static String outTag(final String htmlCode) {
		return htmlCode.replaceAll(
				"<.*?>|\\pP|\\pS|[a-zA-z0-9]| {10,}|[\\s\\p{Zs}]", "");
	}
	
	public static void parser(String link) throws IOException {
		String html = HtmlUtil.JsoupHtml(link);
		String title = HtmlUtil.getTitle(html);
		String keywords = HtmlUtil.getKeywords(html);
		String description = HtmlUtil.getDes(html);
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		
		Saver.HTMLWriter(title, html);
		Saver.TXTWriter(date, title, link, keywords, description);
	}
	
	public void start()  {
		try {
			while (rs.next()) {
				String link;
				try {
					link = rs.getString("url");
					parser(link);
					spider.foundUrl(link);
					System.out.println(link);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		closeAll();
		quit();
	}
	public void quit() {
		System.out.println("网页已经处理完毕,程序退出!");
		//System.exit(0);
	}
	 public void closeAll(){
		 if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stat != null) {
				try {
					stat.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
	 }
		
		/*
		 * 链接数据库的初始化函数
		 */
		public void initDatabase() {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager
						.getConnection("jdbc:mysql://localhost/searchengine?"
								+ "user=root&password=root");
				stat =  conn.createStatement();
				String sql = "SELECT url FROM link";
				rs = stat.executeQuery(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	public static void main(String[] args) throws SQLException, IOException {
		HtmlUtil htmlUtil = new HtmlUtil();
		htmlUtil.initDatabase();
		htmlUtil.start();
		
	} 

}
