<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- /springmvc1/src/main/webapp/WEB-INF/view/user/join.jsp --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>사용자등록</title>
</head>
<body>
<h2>사용자등록</h2>
<%--<form:form : html 태그의 form 태그 , 유효성 검증을 위한 form 설정
modelAttribute="user" : join.jsp 페이지가 실행될때 User객체 필요
 --%>
<form:form modelAttribute="user" method="post" action="join">

<%-- 오류 메세지의 코드에 해당하는 메세지 출력 --%>
<spring:hasBindErrors name="user">
<font color="red">

<%--errors.globalErrors: Controller에서 BindingResult객체에서 reject() 메서드로 설정한 코드값들 --%>
	<c:forEach items="${errors.globalErrors}" var="error">
		<spring:message code="${error.code}"/>
	</c:forEach>
</font>
</spring:hasBindErrors>
<table border="1" style="border-collapse: collapse;" class="w3-table">
<tr>
	<td>아이디</td>
	<td><form:input path="userid" onkeyup="idChk(this.value)" class="w3-input" />
		<font color="red"><form:errors path="userid"/></font></td>
</tr>
<tr>
	<td>비밀번호</td>
	<td><form:password path="password" class="w3-input" />
		<font color="red"><form:errors path="password" /></font></td>
</tr>
<tr>
	<td>이름</td>
	<td><form:input path="username" class="w3-input" />
		<font color="red"><form:errors path="username" /></font></td>
</tr>
<tr>
	<td>전화번호</td>
	<td><form:input path="phoneno" class="w3-input" />
		<font color="red"><form:errors path="phoneno"/></font></td>
</tr>
<tr>
	<td>우편번호</td>
	<td><form:input path="postcode" class="w3-input" />
	<font color="red"><form:errors path="postcode"/></font></td>
</tr>
<tr>
	<td>주소</td>
	<td><form:input path="address" class="w3-input" />
	<font color="red"><form:errors path="address" /></font></td>
</tr>
<tr>
	<td>이메일</td>
	<td><form:input path="email" class="w3-input" />
	<font color="red"><form:errors path="email" /></font></td>
</tr>
<tr>
	<td>생년월일</td>
	<td><form:input path="birthday" class="w3-input" />
	<font color="red"><form:errors path="birthday" /></font></td>
</tr>
<tr><td colspan="2" align="center">
	<input type="submit" value="등록" class="w3-input" >
	<input type="reset" value="초기화" class="w3-input" ></td>
	</tr></table></form:form></body></html>