<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isErrorPage="true" %>
<%--/springmvc1/src/main/webapp/WEB-INF/view/exception.jsp 
	isErrorPage="ture" : exception객체 (exception.LoginException)

--%>
<script>
	alert("${exception.message}")
	location.href='${exception.url}'
</script>