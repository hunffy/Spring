<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- /WEB-INF/view/user/login.jsp --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html><html><head><meta charset="UTF-8">
<title>로그인화면</title>
<script type="text/javascript">
function win_open(page) {
	   var op = "width=500, height=350, left=50,top=150";
	   open(page,"",op);
}
</script>
</head><body>
<h2>사용자 로그인</h2>
<form:form modelAttribute="user" method="post" action="login" name="loginform">
<input type="hidden" name="username" value="유효성검증을위한 파라미터" >
<input type="hidden" name="email" value="valid@aaa.bbb" >

  <spring:hasBindErrors name="user">
    <font color="red"><c:forEach items="${errors.globalErrors}" var="error">
         <spring:message code="${error.code}" />
      </c:forEach></font></spring:hasBindErrors>
      
  <table border="1" style="border-collapse: collapse;" class="w3-table">
   <tr height="40px"><td>아이디</td><td><form:input path="userid" class="w3-input"/>
     <font color="red"><form:errors path="userid" /></font></td></tr>
<tr height="40px"><td>비밀번호</td><td><form:password path="password" class="w3-input"/>

<font color="red"><form:errors path="password" /></font></td></tr>
<tr height="40px"><td colspan="2" class="w3-center">
<button type="submit" class="w3-button w3-blue">로그인</button>
<button onclick="location.href='join'" class="w3-button w3-blue" type="button">회원가입</button>
<button onclick="win_open('idsearch')" class="w3-button w3-blue" type="button">아이디찾기</button>
<button onclick="win_open('pwsearch')" class="w3-button w3-blue" type="button">비밀번호찾기</button>
   </td></tr></table>
</form:form></body></html>