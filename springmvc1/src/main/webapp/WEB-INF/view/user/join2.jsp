<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- /springmvc1/src/main/webapp/WEB-INF/view/user/join.jsp --%>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>  
<%@ taglib prefix="spring" 
                     uri="http://www.springframework.org/tags"%>  
<%@ taglib prefix="form" 
                 uri="http://www.springframework.org/tags/form"%>  
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>사용자등록</title>
</head>
<body>
<h2>사용자 등록</h2>
<form:form modelAttribute="user" method="post" action="join">
<spring:hasBindErrors name="user">
<font color="red">
  <c:forEach items="${errors.globalErrors }" var="error">
    <spring:message code="${error.code}"/>
  </c:forEach>
</font>
</spring:hasBindErrors>

<div class="w3-row w3-section">
  <div class="w3-col" style="width:50px"><i class="w3-xxlarge fa fa-user"></i></div>
    <div class="w3-rest">
      <form:input class="w3-input w3-border" path="userid"  placeholder="아이디" onkeyup="idChk(this.value)" />
      <font color="red"><form:errors path="userid" /></font>
    </div>
</div>
<div class="w3-row w3-section">
  <div class="w3-col" style="width:50px"><i class="w3-xxlarge fa fa-user"></i></div>
    <div class="w3-rest">
      <form:password path="password"  class="w3-input w3-border"  placeholder="비밀번호" /> 
      <font color="red"><form:errors	path="password" /></font>
    </div>
</div>
<div class="w3-row w3-section">
  <div class="w3-col" style="width:50px"><i class="w3-xxlarge fa fa-pencil"></i></div>
    <div class="w3-rest">
      <form:input class="w3-input w3-border" path="username"  placeholder="이름"  />
      <font color="red"><form:errors path="username" /></font>
    </div>
</div>
<div class="w3-row w3-section">
  <div class="w3-col" style="width:50px"><i class="w3-xxlarge fa fa-phone"></i></div>
    <div class="w3-rest">
      <form:input class="w3-input w3-border" path="phoneno"  placeholder="전화번호"  />
      <font color="red"><form:errors path="phoneno" /></font>
    </div>
</div>
<div class="w3-row w3-section">
  <div class="w3-col" style="width:50px"><i class="w3-xxlarge fa fa-envelope-o"></i></div>
    <div class="w3-rest">
      <form:input class="w3-input w3-border" path="postcode"  placeholder="우편번호"  />
      <font color="red"><form:errors path="postcode" /></font>
    </div>
</div>
<div class="w3-row w3-section">
  <div class="w3-col" style="width:50px"><i class="w3-xxlarge fa fa-envelope-o"></i></div>
    <div class="w3-rest">
      <form:input class="w3-input w3-border" path="email"  placeholder="이메일"  />
      <font color="red"><form:errors path="email" /></font>
    </div>
</div>
<div class="w3-row w3-section">
  <div class="w3-col" style="width:50px"><i class="w3-xxlarge fa fa-user"></i></div>
    <div class="w3-rest">
      <form:input class="w3-input w3-border" path="birthday"  placeholder="생년월일"  />
      <font color="red"><form:errors path="birthday" /></font>
    </div>
</div>
<button class="w3-button w3-block w3-section w3-blue w3-ripple w3-padding">회원등록</button>
</form:form>
</body>
</html>