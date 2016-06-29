package com.searchengine.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;



import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;




/**
 * 爬虫类
 * @author BinG
 *
 */
public class Spider extends JFrame implements Runnable{
	Connection conn = null;
	Statement stat = null;
	Dao dao = null;
	private List<String> todo;
	private boolean cancel = false;
	
	private static final long serialVersionUID = 1L;
	//输入框提示文字
	JLabel promoteLabel = new JLabel();
	//开始按钮
	JButton begin = new JButton();
	//清空按钮
	JButton clear = new JButton();
	//下载网页按钮
	JButton download = new JButton();
	//建立索引
	JButton  index = new JButton();
	//输入框
	JTextField url = new JTextField();
	//正在爬取
	JLabel current = new JLabel();

	JScrollPane scroll = new JScrollPane();
	//提示框
	JTextArea mess = new JTextArea();
	JLabel yan_mess = new JLabel();
	//开始爬虫线程
	protected Thread bgThread;
	//处理下载网页线程
	protected Thread dlThread;
	//建立索引线程
	protected Thread inThread;
	//掀桌子线程
	protected Thread xianzhuoziThread;
	String []zhuozi = {"下面跟我一起掀桌子",
			"无表情掀 \r\n( ╯-_-)╯┴—┴ ","桌放好\r\n ┬─┬",
			"无语掀 \r\n（╯#-_-)╯╧═╧","桌放好\r\n ┬─┬",
			"娇蛮掀\r\n (/= _ =)/~┴┴","桌放好\r\n ┬─┬",
			"大力掀\r\n(╯#-_-)╯~~~~╧═╧","桌放好\r\n ┬─┬",
			"单手掀\r\n╭(￣▽￣)╯╧═╧","桌放好\r\n ┬─┬",
			"淡定掀\r\n(╯°Д°)╯︵ ┻━┻","桌放好\r\n ┬─┬",
			"桌子飞人\r\n╧═╧(-_-)╧═╧"
			};
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
	
	//开始爬虫
	public void begin() throws SQLException{
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
		current.setText("(ﾉ･ω･)ﾉﾞ  HI~");
	}
	//获取所有链接
	public void getAllLinks(String url) throws SQLException{
		Document doc;
		try {
			doc = Jsoup.connect(url).get();
			Elements links = doc.select("a[href]");
			for(Element e : links){
				String temp = e.attr("abs:href");
				if(temp!="" && !"".equals(temp) && doc.title()!="" && !"".equals(doc.title()))
				todo.add(temp);
				String link_md5;
				link_md5 = MD5Util.MD5(temp);
				Date now = new java.util.Date();
				SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String date = dateformat.format(now);
				String msg = addLinks(temp, date, link_md5);
				System.out.println(msg);
				foundUrl(msg);
				
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			UpdateErrors err = new UpdateErrors();
			err.msg = url+"地址错误！\r\n";
			System.err.println(url+"地址错误！\r\n");
			SwingUtilities.invokeLater(err);
		}
		
	}
	public String addLinks(String url,String date,String link_md5) throws SQLException{
		ResultSet rs = dao.getResultSetMD5(stat, link_md5);
		int i = 0;
        while (rs.next()) {
			i++;
		}
		if(i == 0){
			String sqlstmt = "insert into link (url,date,link_md5) values('" + url + "','"
					+ date + "','" + link_md5 + "')";
	       dao.SavaData(stat, sqlstmt);
	       return url+"已插入！";
		}else{
			return url+"已存在！";
		}
		
	}
	//下载并处理网页
	public void disposeHtml(){
		ResultSet rs = dao.getResultSetAll(stat);
		try {
			while (rs.next()) {
				String link;
				try {
					link = rs.getString("url");
					HtmlUtil.parser(link);
					foundHtml(link);
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
	}
	public void initDatabase() {
		dao = new Dao();
		conn = dao.getConnection();
		stat = dao.getStatement(conn);
	}
	 public void clear(){
		 todo.clear();
	 }

	 
		public void init(){
			setSize(550, 350);
			setLocation(500, 200);
			setVisible(true);
			setTitle("爬虫和索引器");
			getContentPane().setLayout(null);

			//添加输入提示
			promoteLabel.setBounds(12, 12, 84, 12);
			promoteLabel.setText("输入URL");
			getContentPane().add(promoteLabel);
			//添加开始按钮
			begin.setBounds(310, 36, 94, 24);
			begin.setText("开始爬虫");
			begin.setActionCommand("Begin");
			getContentPane().add(begin);
			//添加清空按钮
			clear.setBounds(410, 36, 60, 24);
			clear.setText("清空");
			getContentPane().add(clear);
			//添加下载按钮
			download.setBounds(410, 140, 94, 30);
			download.setText("处理网页");
			getContentPane().add(download);
			//添加索引按钮
			index.setBounds(410, 200, 94, 30);
			index.setText("建立索引");
			getContentPane().add(index);
			//添加输入框
			url.setBounds(12, 36, 288, 24);
			url.setText("www.hao123.com");
			getContentPane().add(url);
			//掀桌子
			yan_mess.setBounds(410, 250, 94, 30);
			//yan_mess.setText("aaaaaaa");
			getContentPane().add(yan_mess);
			//添加提示框
			scroll.setBounds(12, 120, 384, 156);
			scroll.setAutoscrolls(true);
			scroll
					.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			scroll
					.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			scroll.setOpaque(true);
			mess.setEditable(false);
			mess.setBounds(0, 0, 366, 138);
			scroll.getViewport().add(mess);
			getContentPane().add(scroll);
			current.setText("(ﾉ･ω･)ﾉﾞ  HI~");
			current.setBounds(20, 80, 384, 12);
			getContentPane().add(current);
			beginningActionListener begActListener = new beginningActionListener();
			begin.addActionListener(begActListener);
			//清空按钮监听器
			clear.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					url.setText("");
				}
			});
			
			//下载按钮监听器
			download.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					if(dlThread==null){
						DownloadT dt = new DownloadT();
						dlThread = new Thread(dt);
						dlThread.start();
						//xianzhuozi(true);
					}else{
						dlThread.stop();
						dlThread = null;
						download.setText("处理网页");
						current.setText("(ﾉ･ω･)ﾉﾞ  HI~");
						//xianzhuozi(false);
						mess.setText("已结束网页处理！\r\n");
					}
					
						
				}
			});
			//索引按钮监听器
			index.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					BulidIndexT bit = new BulidIndexT();
					inThread = new Thread(bit);
					inThread.start();
					mess.setText("正在建立索引...请稍后...");
				}
			});
		}
		
		//下载线程
	class DownloadT implements Runnable{

			public void run() {
				// TODO Auto-generated method stub
				download.setText("停止处理");
				mess.setText("正在处理并下载网页！\r\n");
				initDatabase();
				disposeHtml();
			}
			
		}
	//索引线程
	class BulidIndexT implements Runnable{

		public void run() {
			new Indexer().start("H:\\search\\text\\", "H:\\search\\index\\");
			mess.setText("索引建立完毕！\r\n");
		}
		
	}
	//掀桌子线程
	class xianzhuozi implements Runnable{

		public void run() {
			// TODO Auto-generated method stub
			while (true) {
				for (int i = 0; i < zhuozi.length; i++) {
					yan_mess.setText(zhuozi[i]);
				}
				
			}
		}
		
	}
	public void xianzhuozi(boolean b){
		xianzhuozi xzz = new xianzhuozi();
		xianzhuoziThread = new Thread(xzz);
		if(b){
			xianzhuoziThread.start();
			try {
				xianzhuoziThread.wait(2000);
				xianzhuoziThread.notify();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else {
			xianzhuoziThread.stop();
		}
		
	}
		//开始按钮监听器
		class beginningActionListener implements ActionListener {
			public void actionPerformed(ActionEvent event) {
				Object object = event.getSource();
				if (object == begin)
					beginningActionPerformed(event);
			}
		}
		void beginningActionPerformed(ActionEvent event) {
			if (bgThread == null) {
				begin.setText("停止爬虫");
				bgThread = new Thread(this);
				bgThread.start();
				//xianzhuozi(true);
			} else {
				cancel();
				
			}

		}
		public void foundUrl(String str){
			UpdateCurrentStats ucs = new UpdateCurrentStats();
			ucs.msg = str;
			SwingUtilities.invokeLater(ucs);
		}
		public void foundHtml(String str){
			UpdateCurrentStatsByHtml ucsbt = new UpdateCurrentStatsByHtml();
			ucsbt.msg = str;
			SwingUtilities.invokeLater(ucsbt);
		}
		class UpdateCurrentStats implements Runnable {
			public String msg;

			public void run() {
				current.setText("正在爬取: " + msg);
			}
		}
		class UpdateCurrentStatsByHtml implements Runnable {
			public String msg;

			public void run() {
				current.setText("正在处理: " + msg);
			}
		}
		class UpdateErrors implements Runnable {
			public String msg;

			public void run() {
				mess.append(msg);
			}
		}
		public void run() {
			// TODO Auto-generated method stub
			try {
				mess.setText("");
				//clear();
				List<String> list = new ArrayList<String>();
				if(url.getText().trim()==""||"".equals(url.getText().trim())){
					list.add("http://www.hao123.com");
					url.setText("www.hao123.com");
				}else{
					list.add("http://"+url.getText());
				}
				setTodo(list);
				initDatabase();
				begin();
				Runnable doLater = new Runnable() {
					public void run() {
						begin.setText("开始爬虫");
						current.setText("(ﾉ･ω･)ﾉﾞ  HI~");
						//xianzhuozi(false);
					}
				};
				
				SwingUtilities.invokeLater(doLater);
				bgThread = null;

			} catch (Exception e) {
				// TODO Auto-generated catch block
				UpdateErrors err = new UpdateErrors();
				err.msg = "http://"+url.getText()+"地址错误！\r\n";
				System.err.println("http://"+url.getText()+"地址错误！\r\n");
				SwingUtilities.invokeLater(err);
			}
		}
		public static void main(String[] args) {
			Spider spider = new Spider();
			spider.init();
			
			
		}
}
