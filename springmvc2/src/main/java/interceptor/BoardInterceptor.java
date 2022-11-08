package interceptor;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import logic.User;

public class BoardInterceptor extends HandlerInterceptorAdapter{

	@Override   //실행전 호출 메서드
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession(); //세션 객체
		//boardid : 게시판 종류 
		String boardid = (String)session.getAttribute("boardid");
		//로그정보
		User login = (User)session.getAttribute("loginUser");
		if(boardid == null || boardid.equals("1")) { //공지사항
		  if(login == null || !login.getUserid().equals("admin")) {
			  //url에 표시될수 있는 메세지로 변환 
			  String msg = URLEncoder.encode
					  ("관리자만 등록 가능합니다.","UTF-8");
			  //request.getContextPath() :  프로젝명. 웹어플리케이션명
			  //                             /springmvc2
			  response.sendRedirect
				(request.getContextPath()+"/board/list?boardid="
				+boardid+"&msg="+msg);
			  return false; //Controller 메서드 호출 안함.
	                //BoardController.getBoard() 메서드 호출 안함
		  }
		}
		return true;//BoardController.getBoard() 메서드 호출 함
	}
	
}