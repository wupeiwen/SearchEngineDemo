<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>爬虫程序</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script src="http://libs.baidu.com/jquery/2.0.0/jquery.js"></script>
	<style type="text/css">
		.container{
			margin:0 auto;
			width:80%;
			height:140;
			border:1px solid green;
			border-bottom:none;
			padding: 100px 100px;
		}
		button{
			border:1px solid black;
			background:white;
			color:black;
			margin: 20px auto;
			width: 102px;
			height: 40px;
			font-size: 22px;
		}
		input{
		width: 536px;
		height: 40px;
		}
		button:hover{
			border:1px solid black;
			background:gray;
			color:white;
		}
		
		
	
		
	</style>
	<script type="text/javascript">
	 function begin(){
		$.ajax({
		type:"post",
		url:"spider",
		async:true,
		data:$("#form").serializeArray(),
		dataType:"text",
		success:function(data){
		alert('aaaaaa');
		$("#haha").html(data);
	}
	});
	}
	</script>
  </head>
  
  <body>
   <div class="container">
   <form name="form" id="form">
				<fieldset align="center">
				    <legend align="center" style="font-size: 70px;">爬虫程序</legend>
				  	  起始URL：<input name="link" id="link">
				   <!--  <button type="submit" name="submit" id="submit">爬取</button> -->
				    <button type="button" name="button" id="button" onclick="javascript:begin();">爬取</button>
				</fieldset>
	</form>
	<div id="haha">

	</div>
    	</div>
  </body>
</html>
