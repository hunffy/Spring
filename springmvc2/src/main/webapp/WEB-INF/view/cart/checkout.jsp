<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>    
<%-- /springmvc1/src/main/webapp/WEB-INF/view/cart/checkout.jsp --%>
<!DOCTYPE html><html><head><meta charset="UTF-8">
<title>주문 전 상품 목록 보기</title></head>
<body><h2>배송지 정보</h2>
<table class="w3-table-all"><tr><td width="30%">주문아이디</td>
      <td width="70%">${sessionScope.loginUser.userid}</td></tr>
  <tr><td>이름</td><td>${sessionScope.loginUser.username}</td></tr>
  <tr><td>우편번호</td><td>${sessionScope.loginUser.postcode}</td></tr>
  <tr><td>주소</td><td>${sessionScope.loginUser.address}</td></tr>
  <tr><td>전화번호</td><td>${sessionScope.loginUser.phoneno}</td></tr>
</table><h2>구매 예정 상품 </h2>
<table class="w3-table-all"><tr><th>상품명</th><th>가격</th><th>수량</th><th>합계</th></tr>
  <c:forEach items="${sessionScope.CART.itemSetList}"  var="itemSet" varStatus="stat">
   <tr><td>${itemSet.item.name}</td>
   <td><fmt:formatNumber value="${itemSet.item.price}" pattern="###,###"/></td>
   <td><fmt:formatNumber value="${itemSet.quantity}" pattern="###,###"/></td>
   <td><fmt:formatNumber value="${itemSet.item.price *itemSet.quantity}" pattern="###,###"/></td></tr>
  </c:forEach>
  <tr><td colspan="4" align="right">
          총 구입 금액 : <fmt:formatNumber value="${sessionScope.CART.total}" pattern="###,###"/>원</td></tr>
  <tr><td colspan="4"><a href="end">주문확정</a>&nbsp;
     <a href="../item/list">상품 목록</a>&nbsp;
  </td></tr></table></body></html>