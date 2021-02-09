<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<body>
{
	"code": "000000",
	"message": "SUCCESS",
	"data": "sessionid":${sessionid}
}
	
<h3>欢迎${username }登录</h3>
	<br>
	<form action="uploadApp.do" method="post">
		<table>
			<tr>
				<td><input id="submit" type="submit" value="上传"></td>
			</tr>
		</table>
	</form>
	<br>
	<form action="updateAppInfo.do" method="post">
		<table>
			<tr>
				<td><label>appId：</label></td>
				<td><input type="text" id="appId" name="appId"></td>
			</tr>
			<tr>
				<td><label>name：</label></td>
				<td><input type="text" id="name" name="name"></td>
			</tr>
			<tr>
				<td><label>desc：</label></td>
				<td><input type="text" id="desc" name="desc"></td>
			</tr>
			<tr>
				<td><input id="submit" type="submit" value="修改"></td>
			</tr>
		</table>
	</form>
	
	<br>
	<form action="deleteApp.do" method="post">
		<table>
			<tr>
				<td><label>appId：</label></td>
				<td><input type="text" id="appId" name="appId"></td>
			</tr>
			<tr>
				<td><input id="submit" type="submit" value="删除"></td>
			</tr>
		</table>
	</form>
	
	<br>
	<form action="queryAppList.do" method="post">
		<table>
			<tr>
				<td><input id="submit" type="submit" value="查询"></td>
			</tr>
		</table>
	</form>
	
</body>
</html>