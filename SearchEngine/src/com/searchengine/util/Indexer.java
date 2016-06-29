package com.searchengine.util;
/**
 * 对指定的文件建立索引
 */
import java.io.File;
import java.io.IOException;

import jeasy.analysis.MMAnalyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.store.LockObtainFailedException;

public class Indexer {
	
	public static void start(String dataDir, String indexDir) {
		File dataFile = new File(dataDir);
		File indexFile = new File(indexDir);
		index(dataFile, indexFile);
		quit();
	}
	/**
	 * 对指定文件夹进行分词并建立索引
	 * @param dataFile
	 * @param indexFile
	 */
	private static void index(File dataFile, File indexFile) {
		if (!dataFile.exists() || !dataFile.isDirectory()) {
			System.out.println("Error message:" + dataFile + " 文件夹不存在.");
		}
        //中文分词器
		Analyzer zh_cnAnalyzer = new MMAnalyzer();
		IndexWriter writer;
			try {
				writer = new IndexWriter(indexFile, zh_cnAnalyzer, true, MaxFieldLength.UNLIMITED);
				indexDirectory(writer, dataFile);
				writer.optimize();
				writer.close();
			} catch (CorruptIndexException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (LockObtainFailedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	private static void indexDirectory(IndexWriter writer, File dir) {
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			if (f.isDirectory()) {
				indexDirectory(writer, f);
			} else if (f.getName().endsWith(".text")) {
				indexFile(writer, f);
			}
		}
	}

	private static void indexFile(IndexWriter writer, File f) {
		if (f.isHidden() || !f.exists() || !f.canRead()) {
			return;
		}
		Document doc = Docer.fileToDoc(f);
			try {
				writer.addDocument(doc);
			} catch (CorruptIndexException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	private static void quit() {
		System.out.println("索引建立完毕.");
		//System.exit(0);
	}
	public static void main(String[] args) {
		new Indexer().start("H:\\search\\text\\", "H:\\search\\index\\");
	}
	
}
