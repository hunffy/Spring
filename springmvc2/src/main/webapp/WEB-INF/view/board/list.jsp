<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- /springmvc2/src/main/webapp/WEB-INF/view/board/list.jsp --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html><head><meta charset="UTF-8">
<title>게시판 목록 보기</title>
<script>
   if ('${param.msg}') { //msg 파라미터가 존재하면
	   alert('${param.msg}') 
   }
</script>
</head>
<body>
<table class="w3-table-all">
  <tr><td colspan="4">${boardName} 게시판</td><td>글개수:${listcount}</td></tr>
 <c:if test="${listcount > 0}">
  <tr><th>번호</th><th>제목</th><th>글쓴이</th><th>날짜</th><th>조회수</th></tr>
<%-- 글목록 출력 --%>
 <c:forEach var="board" items="${boardlist}">
  <tr><td>${boardno}</td>
      <c:set var="boardno" value="${boardno - 1}" />
      <td style="text-align: left;">
      <c:if test="${! empty board.fileurl}">
         <a href="file/${board.fileurl}">@</a></c:if>
      <c:if test="${empty board.fileurl}">&nbsp;&nbsp;&nbsp;</c:if>
  <c:forEach begin="1" end="${board.grplevel}">&nbsp;&nbsp;</c:forEach>
      <c:if test="${board.grplevel > 0}">└</c:if> <%-- ㅂ한자 --%> 
    <a href="detail?num=${board.num}">${board.subject}</a></td>
      <td>${board.writer}</td>
      <td><fmt:formatDate value="${board.regdate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
      <td>${board.readcnt}</td></tr>
 </c:forEach>
 
 <%-- 페이징 부분 --%>
 <tr><td colspan="5">
  <div class="w3-center w3-padding-32">
    <div class="w3-bar">
     <c:if test="${pageNum > 1}">
       <a href="list?pageNum=${pageNum - 1}&boardid=${boardid}">[이전]</a></c:if>
       <c:if test="${pageNum <= 1}">[이전]</c:if>
     <c:forEach var="a" begin="${startpage }" end="${endpage}">
         <c:if test="${a == pageNum}">
           <a class="w3-button w3-black" href="#">${a}</a></c:if>
         <c:if test="${a != pageNum}">
           <a class="w3-button w3-hover-black" 
              href="list?pageNum=${a}&boardid=${boardid}">${a}</a></c:if>
     </c:forEach>
     <c:if test="${pageNum < maxpage}">
           <a href="list?pageNum=${pageNum + 1}&boardid=${boardid}">[다음]</a></c:if>
     <c:if test="${pageNum >= maxpage}">[다음]</c:if></td></tr>
    </div>
  </div>
</c:if>
<c:if test="${listcount == 0}">
     <tr><td colspan="5">등록된 게시물이 없습니다.</td></tr>
 </c:if>
   <c:if test="${!empty param.boardid && param.boardid != '1'}">
    <tr><td colspan="5" align="right">
       <a href="write">[글쓰기]</a></td></tr>
   </c:if>      
   <%-- 공지사항인 경우 관리자만 글쓰기 가능 --%>
   <c:if test="${empty param.boardid || param.boardid == '1'}">
     <c:if test="${loginUser.userid == 'admin' }">
     <tr><td colspan="5" align="right">
       <a href="write">[글쓰기]</a></td></tr>
     </c:if>
   </c:if>      
</table></body></html>