<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="org.apache.lucene.document.Document"%>
<%@ page import="org.apache.lucene.analysis.Analyzer"%>
<%@ page import="org.apache.lucene.search.highlight.Highlighter"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<!-- <link type="text/css" rel="stylesheet"
			href="/anysearch/css/result.css" /> -->
		<title>搜索结果</title>
		<script type="text/javascript">
function clrSrcMsg() {
	var srcMsg = document.getElementById("account");
	if(srcMsg != null) {
		srcMsg.value = "";
	}
}
</script>
	<style type="text/css">
		.container{
			margin:0 auto;
			width:80%;
			height:140;
			border:1px solid green;
			border-bottom:none;
			padding: 10px;
		}
		button{
			border:1px solid black;
			background:white;
			color:black;
			margin: 20px auto;
			width: 73px;
			height: 32px;
			font-size: 16px;
		}
		input{
		width: 400px;
		height: 30px;
		}
		button:hover{
			border:1px solid black;
			background:gray;
			color:white;
		}
		
	
		
	</style>
	</head>
	<%
		int totalNum = 0;
		int numPerPage = 10;
		int maxPageNum = 0;
		int curPage = 1;
		int startLocation = 0;

		List<Document> list = new ArrayList<Document>();
		list = (ArrayList<Document>) request.getAttribute("docList");
		totalNum = ((Integer) request.getAttribute("totalCount"))
				.intValue();

		String keyWords = (String) request.getAttribute("keyWords");
		totalNum = list.size();
		maxPageNum = ((totalNum % numPerPage) == 0) ? totalNum / numPerPage
				: (totalNum / numPerPage + 1);
		curPage = startLocation / numPerPage + 1;
		Analyzer analyzer = (Analyzer) request.getAttribute("analyzer");
		Highlighter highlighter = (Highlighter) request
				.getAttribute("highlighter");
	%>

	<%
		if (request.getParameter("startLocation") != null) {
			startLocation = Integer.parseInt(request
					.getParameter("startLocation")); //取当前页码
			System.out.println("startLocation =" + startLocation);
			curPage = ((startLocation % numPerPage) == 0) ? (startLocation / numPerPage)
					: (startLocation / numPerPage + 1);
			curPage = curPage + 1;
			System.out.println("curPage =" + curPage);
		}
	%>

	<body>
	<fieldset>
				    <legend style="font-size: 30px;">GG搜索</legend>
		<div id="div1">
			<div class="div12">
				<form name="searchForm" action="search" method="post">
					
					<input name="keyWords" id="keyWords"  value="<%=keyWords%>" onfocus="clrSrcMsg()">
				    <button type="submit" name="submit" id="submit">搜索</button><br/>
					<font color="#000000" size="-1">找到约</font><font color="red" size="-1"> <%out.print(totalNum);%> 
					</font><font color="#000000" size="-1">个相关网页</font>
						
					
				</form>
			</div>
		</div>
		<div id="div2">
			<div class="div22">
				<%
					if (curPage < maxPageNum) {
						for (int i = startLocation; i < startLocation + numPerPage; i++) {
				%>
				<div class="div">
					<table border="0">
						<tr>
							<td>
								<a class="a1" href="<%out.print(list.get(i).get("link")); %>" target="_blank">
									<%
										String titleStr = highlighter.getBestFragment(analyzer,
														"title", list.get(i).get("title").replaceAll(
																".text", ""));
				                                %>
												<font size="+1" color="#0000CC">
												<%
												if (titleStr != null) {
													out.print(titleStr);
												} else {
													out.print(list.get(i).get("title").replaceAll(".text",
															""));
												}%>
												</font>
									 </a>
								<br />
								<%
									String bodyStr = highlighter.getBestFragment(analyzer,
													"content", list.get(i).get("content").replaceAll(" ",""));%>
											<font size="-1">
											<%
											if (bodyStr != null) {
												out.print(bodyStr);
											} else {
												out.print(list.get(i).get("content").replaceAll(" ",""));
											}
											%>
											</font>
							
								<br/><font color="#008000"> <%
 	if (list.get(i).get("link").length() > 35)
 				out.print(list.get(i).get("link").substring(0, 35));
 			else
 				out.print(list.get(i).get("link"));
 				%></font>
 				<%-- <font size = "-1" color="black">
 				<%
 				out.print("-<a href=" + "#" + ">" + "网页快照" + "</a>");
 %> </font> --%>
							</td>
						</tr>
					</table>
				</div>
				<%
					}
					} else {
						for (int i = startLocation; i < totalNum; i++) {
				%>
				<div class="div">
					<table border="0">
						<tr>
							<td>
								<a class="a1" href="<%out.print(list.get(i).get("link")); %>">
									<%
										String titleStr = highlighter.getBestFragment(analyzer,
														"title", list.get(i).get("title").replaceAll(
																".text", ""));
				                                %>
												<font size="+1" color="#0000CC">
                                                <%
												if (titleStr != null) {
													out.print(titleStr);
												} else {
													out.print(list.get(i).get("title").replaceAll(".text",
															""));
												}%>
												</font>
									 </a>
								<br />
								<%
									String bodyStr = highlighter.getBestFragment(analyzer,
													"content", list.get(i).get("content").replaceAll(" ",""));%>
											<font size="-1">
											<%
											if (bodyStr != null) {
												out.print(bodyStr);
											} else {
												out.print(list.get(i).get("content").replaceAll(" ",""));
											}
											%>
											</font>
								<br/><font color="#3C8B1D"> <%
 	if (list.get(i).get("link").length() > 35)
 				out.print(list.get(i).get("link").substring(0, 35));
 			else
 				out.print(list.get(i).get("link"));
 				%></font>
 				<font size = "-1" color="black">
 				<%
 				out.print("-<a href=" + "#" + ">" + "网页快照" + "</a>");
 %> </font>
							</td>
						</tr>
					</table>
				</div>
				<%
					}
					}
				%>
				<div class="div23">
					<form method="post">
						<table>
							<tr>
								<td>
									<%
										if (curPage > 1) {
											out
													.println("<a href='search?startLocation="
															+ (startLocation - numPerPage)
															+ "&&keyWords="
															+ keyWords);
											out.println("'>上一页</a>");
										}
										if (maxPageNum > 10) {
											int totalNumShow = curPage + 2;
											int startLocationPage = 0;
											if (totalNumShow > maxPageNum) {
												totalNumShow = maxPageNum;
											}
											if (maxPageNum > 11 && curPage > 11) {
												startLocationPage = curPage - 11;
											}
											for (int i = startLocationPage; i < totalNumShow; i++) {
												if (i + 1 == curPage) {
									%>&nbsp;[<%=i + 1%>]&nbsp;<%
										} else {
									%><a
										href="search?startLocation=<%=i * numPerPage%>&&keyWords=<%=keyWords%>">&nbsp;<%=i + 1%>&nbsp;</a>
									<%
										}
											}
										} else {
											for (int i = 0; i < maxPageNum && maxPageNum > 1; i++) {
												if (i + 1 == curPage) {
									%>&nbsp;[<%=i + 1%>]&nbsp;<%
										} else {
									%><a
										href="search?startLocation=<%=i * numPerPage%>&&keyWords=<%=keyWords%>">&nbsp;<%=i + 1%>&nbsp;</a>
									<%
										}
											}
										}

										if (curPage < maxPageNum) {
											out.println("<a href='search?startLocation="
															+ (startLocation + numPerPage)
															+ "&&keyWords="
															+ keyWords);
											out.println("'>下一页</a>");
										}
									%>
								</td>
							</tr>
						</table>
					</form>
				</div>
			</div>
		</div>
</fieldset>
	</body>
</html>
