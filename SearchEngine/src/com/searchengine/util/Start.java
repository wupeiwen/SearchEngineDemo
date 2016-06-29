package com.searchengine.util;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;





public class Start extends JFrame implements Runnable{

	/**
	 * @param args
	 */

	private static final long serialVersionUID = 1L;
	//输入框提示文字
	JLabel promoteLabel = new JLabel();
	//开始按钮
	JButton begin = new JButton();
	//输入框
	JTextField url = new JTextField();
	//正在爬取
	JLabel current = new JLabel();
	//提示框
	JScrollPane scroll = new JScrollPane();
	JTextArea mess = new JTextArea();
	Spider spider;
	protected Thread bgThread;
	public void init(){
		setSize(440, 350);
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
		//添加输入框
		url.setBounds(12, 36, 288, 24);
		getContentPane().add(url);

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
		current.setText("正在爬取: ");
		current.setBounds(12, 72, 384, 12);
		getContentPane().add(current);
		beginningActionListener begActListener = new beginningActionListener();
		begin.addActionListener(begActListener);
	}
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
		} else {
			spider.cancel();
		}

	}
	public void foundUrl(String str){
		UpdateCurrentStats ucs = new UpdateCurrentStats();
		ucs.msg = str;
		SwingUtilities.invokeLater(ucs);
	}
	class UpdateCurrentStats implements Runnable {
		public String msg;

		public void run() {
			current.setText("正在爬取: " + msg);
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
			spider = new Spider();
			//spider.clear();
			List<String> list = new ArrayList<String>();
			list.add("http://"+url.getText());
			spider.setTodo(list);
			spider.initDatabase();
			spider.begin();
			//foundUrl(mess);
			Runnable doLater = new Runnable() {
				public void run() {
					begin.setText("开始爬虫");
				}
			};
			SwingUtilities.invokeLater(doLater);
			bgThread = null;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			UpdateErrors err = new UpdateErrors();
			err.msg = "地址错误！";
			SwingUtilities.invokeLater(err);
		}
	}
	public static void main(String[] args) {
		Start sta = new Start();
		sta.init();
	}

}
