<%@ page language="java" contentType="text/html; charset=UTF-8"   pageEncoding="UTF-8"%>
<%-- /springmvc2/src/main/webapp/WEB-INF/view/board/reply.jsp --%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8">
<title>${boardName }게시판 답글 쓰기</title>
</head>
<body>
<form:form modelAttribute="board" action="reply" method="post" 
          name="f">
  <form:hidden  path="num" /> <!-- 원글의 게시글 번호 -->
  <form:hidden  path="grp" /> <!-- 원글의 그룹 번호 -->
  <form:hidden  path="grplevel" />  <!-- 원글의 답변글 레벨 -->
  <form:hidden  path="grpstep" />   <!-- 원글의 순서 -->
  <form:hidden  path="boardid" />   <!-- 원글의 게시판구분 -->
  <table class="w3-table-all">
     <caption>${boardName } 답글 등록</caption>
  <tr><td>글쓴이</td>
      <td><input type="text" name="writer" 
          class="w3-input w3-border">
         <font color="red"><form:errors path="writer" /></font></td>
  </tr>
  <tr><td>비밀번호</td>
      <td><form:password path="pass"  class="w3-input w3-border"/>
          <font color="red"><form:errors path="pass" /></font></td>
  </tr>
  <tr><td>제목</td>
      <td><form:input path="subject" value="RE:${board.subject}"  class="w3-input w3-border"/> 
          <font color="red"><form:errors path="subject" /></font></td>
  </tr>
  <tr><td>내용</td>
      <td><textarea name="content" rows="15" cols="80"></textarea>
  <script type="text/javascript">
     CKEDITOR.replace("content",{ filebrowserImageUploadUrl : "imgupload"});
  </script>
      <font color="red"><form:errors path="content" /></font></td>
  </tr>
  <tr><td colspan="2" class="w3-center">
     <a href="javascript:document.f.submit()">[답변글등록]</a></td>
  </tr></table>
</form:form></body></html>