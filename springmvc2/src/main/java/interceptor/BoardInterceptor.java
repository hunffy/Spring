package interceptor;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import logic.User;

public class BoardInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, 
			HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession();
		String boardid = (String)session.getAttribute("boardid");
		User login = (User)session.getAttribute("loginUser");
		if(boardid == null || boardid.equals("1")) {
			if(login == null || login.getUserid().equals("admin")) {
				String msg = URLEncoder.encode
						("관리자만 등록 가능합니다","UTF-8");
				response.sendRedirect
				(request.getContextPath()+"/board/list?boardid="
						+boardid+"&msg="+msg);
				return false;
			}
		}
		return true;
	}
	
}
