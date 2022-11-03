package aop;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import exception.LoginException;
import logic.User;

@Component
@Aspect
public class AdminLoginAspect {
	//controller에 AdminController의 모든메서드들은 
	@Around(" execution(* controller.AdminController.*(..)) && args(..,session)")
	public Object adminCheck(ProceedingJoinPoint joinPoint,
							HttpSession session) throws Throwable {
		User loginUser = (User)session.getAttribute("loginUser");
		if(loginUser == null)
			throw new LoginException("[adminCheck]로그인 후 거래하세요","../user/login");
		else if(!loginUser.getUserid().equals("admin"))
			throw new LoginException
			("[adminCheck]관리자만 접근 가능합니다","../user/mypage?id="+loginUser.getUserid());
		return joinPoint.proceed();
	}
}
