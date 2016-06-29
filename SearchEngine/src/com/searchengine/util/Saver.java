package com.searchengine.util;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
/**
 * 对读取的一些文档信息进行保存
 * @author BinG
 *
 */
public class Saver {
	
	//保存HTML类型的文档到本地文件系统
	public static void HTMLWriter(String title,String content){
		File f = new File("H:\\search\\html\\" +title + ".html");
		try {
			FileWriter fw = new FileWriter(f,true);
			fw.write(content);
			fw.close();
		} catch (IOException e) {
            // TODO Auto-generated catch block
			Saver.LOGWriter(title + ".html", "IOException");
		}
	}
	
	//保存TXT类型的文档到本地文件系统
	public static void TXTWriter(String date, String title, String link, String keywords, String description){
		File f = new File("H:\\search\\text\\" +title + ".text");
		try {
			FileWriter fw = new FileWriter(f,true);
			fw.write("author:GuoBin" + "\r\n");
			fw.write("date:" + date + "\r\n");
			fw.write("version:1.0" + "\r\n");
			fw.write("title:" + title + "\r\n");
			fw.write("link:" + link + "\r\n");
			fw.write("keywords:" + keywords + "\r\n");
			fw.write("description:" + description + "\r\n");
			fw.close();
		} catch (IOException e) {
            // TODO Auto-generated catch block
			Saver.LOGWriter(title + ".text", "IOException");
		}
	}
	
	//保存日志信息到本地文件系统
	public static void LOGWriter(String tar, String errMsg) {
		File f = new File("H:\\search\\logs\\" + "log.text");
		try {
			FileWriter fw = new FileWriter(f,true);
			fw.write(tar + "\r\n");
			fw.write(errMsg + "\r\n");
			fw.write("\r\n");
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
