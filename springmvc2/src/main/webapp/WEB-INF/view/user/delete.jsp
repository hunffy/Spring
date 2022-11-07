<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- /springmvc1/src/main/webapp/WEB-INF/view/user/delete.jsp --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
    
<!DOCTYPE html>
<html><head><meta charset="UTF-8"><title>회원 탈퇴</title></head>
<body>
<table>
  <tr><td>아이디</td><td>${user.userid}</td></tr>
  <tr><td>이름</td><td>${user.username}</td></tr>
  <tr><td>생년월일</td><td>
   <fmt:formatDate value="${user.birthday}" pattern="yyyy-MM-dd" /></td></tr>
</table>
<%-- 
	/* 탈퇴 검증
	 * UserLoginAspect.userIdCheck() 메서드 실행 하도록 설정
	 *
	 * 회원탈퇴
	 * 1.파라미터 정보 저장.
	 *   - 관리자인 경우 탈퇴 불가
	 * 2.비밀번호 검증 => 로그인된 비밀번호 검증 
	 *   본인탈퇴 : 본인 비밀번호 
	 *   관리자가 타인 탈퇴 : 관리자 비밀번호
	 * 3.비밀번호 불일치 
	 *   메세지 출력 후 delete 페이지 이동  
	 * 4.비밀번호 일치
	 *   db에서 해당 사용자정보 삭제하기
	 *   본인탈퇴 : 로그아웃, login 페이지 이동
	 *   관리자탈퇴 : admin/list 페이지 이동 => 404 오류 발생 
	 */
--%>	 	
<form method="post" action="delete" name="deleteform">
    <input type="hidden" name="userid" value="${param.id}">
    비밀번호 <input type="password" name="password">
	<a href="javascript:deleteform.submit()">[회원탈퇴]</a>    
  </form></body></html>