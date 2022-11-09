<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%-- /springmvc2/src/main/webapp/WEB-INF/view/board/delete.jsp --%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html><html><head><meta charset="UTF-8">
<title>게시판 삭제 화면</title></head>
<body>
<form action="delete"  method="post"  name="f">
<spring:hasBindErrors name="board">
	<font color="red"><c:forEach items="${errors.globalErrors}"
	var="error"><spring:message code="${error.code }" /></c:forEach>
	</font></spring:hasBindErrors>
<input type="hidden" name="num" value="${param.num}">
<table class="w3-table-all">
    <caption>${boardName}글 삭제 화면</caption>
	<tr><td>게시글비밀번호</td>
		<td><input type="password" name="pass" 
		   class="w3-input w3-border" /></td></tr>
	<tr><td colspan="2">
<a href="javascript:document.f.submit()">[게시글삭제]</a></td></tr>
</table></form></body></html>