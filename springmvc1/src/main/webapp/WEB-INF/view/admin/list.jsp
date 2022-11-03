<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- /springmvc1/src/main/webapp/WEB-INF/view/admin/list.jsp --%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html><html><head>
<meta charset="UTF-8">
<title>회원목록</title>
<script type="text/javascript">
   function allchkbox(allchk) {
	   $(".idchks").prop("checked",allchk.checked)
   }
</script>
<style>
   .noline {text-decoration: none;}
</style>
</head><body>
  <table class="w3-table-all"><tr><td colspan="7">회원목록</td></tr>
  <tr><th>아이디<a href="list?sort=1" class="noline">▲</a>
  			  <a href="list?sort=2" class="noline">▼</a></th>
  <th>이름<a href="list?sort=3" class="noline">▲</a>
  			  <a href="list?sort=4" class="noline">▼</a></th>
  <th>전화</th>
  <th>생일</th>
  <th>이메일<a href="list?sort=5" class="noline">▲</a>
  			  <a href="list?sort=6" class="noline">▼</a></th>
  <th>&nbsp;</th></tr>
<c:forEach items="${list}" var="user">
<tr><td>${user.userid}</td><td>${user.username}</td><td>${user.phoneno}</td>
   <td><fmt:formatDate value="${user.birthday}"  pattern="yyyy-MM-dd"/></td>
   <td>${user.email}</td>
   <td><a href="../user/update?id=${user.userid}">수정</a>
       <a href="../user/delete?id=${user.userid}">강제탈퇴</a>
       <a href="../user/mypage?id=${user.userid}">회원정보</a>
   </td>
</c:forEach>
  </table></body></html>
