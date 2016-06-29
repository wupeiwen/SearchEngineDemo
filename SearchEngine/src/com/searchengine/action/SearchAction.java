package com.searchengine.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jeasy.analysis.MMAnalyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;
/**
 * 搜索控制器
 * @author BinG
 *
 */
@Controller("SearchAction")
@Scope("prototype")
public class SearchAction extends ActionSupport {
	private String keyWords;
	public String search(){
		try {
			if(keyWords.trim() == ""||"".equals(keyWords.trim())){
				return INPUT;
			}
		HttpServletRequest request = ServletActionContext.getRequest();  
		HttpServletResponse response = ServletActionContext.getResponse();  
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		 String searchPath = "H:\\search\\index\\";
		 String startLocationTemp = request.getParameter("startLocation");
			if(startLocationTemp != null) {
			int startLocation = Integer.parseInt(startLocationTemp);
			System.out.println(startLocation);
			}
		 Analyzer analyzer = null;
		 Highlighter highlighter = null;
		 List<Document> list = new ArrayList<Document>();
		 Integer totalCount = null;
		 IndexSearcher indexSearch = new IndexSearcher(searchPath);
		analyzer = new MMAnalyzer();
		String[] field = { "title", "content", "link"};
		Map<String, Float> boosts = new HashMap<String, Float>();
		boosts.put("title", 3f);
		QueryParser queryParser = new MultiFieldQueryParser(field,
				analyzer, boosts);
		Filter filter = null;
		
			Query query = queryParser.parse(keyWords);
			TopDocs topDocs = indexSearch.search(query, filter, 10000,
					new Sort(new SortField("size")));
			totalCount = new Integer(topDocs.totalHits);
			SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter(
					"<font color='red'>", "</font>");
			highlighter = new Highlighter(simpleHTMLFormatter,
					new QueryScorer(query));
			highlighter.setTextFragmenter(new SimpleFragmenter(70));
			for (int i = 0; i < topDocs.totalHits; i++) {
				ScoreDoc scoreDoc = topDocs.scoreDocs[i];
				int docSn = scoreDoc.doc; // 文档内部编号
				Document doc = indexSearch.doc(docSn); // 根据编号取出相应的文档
				list.add(doc);
			}
			request.setAttribute("keyWords", keyWords);
			request.setAttribute("totalCount", totalCount);
			request.setAttribute("docList", list);
			request.setAttribute("analyzer", analyzer);
			request.setAttribute("highlighter", highlighter);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SUCCESS;
	}
	public String getKeyWords() {
		return keyWords;
	}
	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}
	
}
