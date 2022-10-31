package aop;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import exception.LoginException;
import logic.User;

@Component	// 객체화를 실행해
@Aspect		// AOP 기능의 클래스

public class UserLoginAspect {
	/*
	 * Pointcut : 핵심메서드의 선택 기준설정
	 * ->execution(* controller.User*.loginCheck*(..)) " 
	 * :controller패키지의 클래스 중 User로 시작하는 클래스의 메서드 중 loginCheck로 시작하는 (..) 매개변수와 상관없는 모든 메서드
	 * 
	 * args(..,session) :
	 * 메서드의 매개변수의 마지막 변수의 자료형이 HttpSession인 메서드를 선택
	 * .. : 아무거나 상관없음.
	 * 	
	 * Advice : AOP 메서드의 실행되는 시점 설정
	 * 	@Around : 핵심(Pointcut) 메서드 실행 전 후 AOP메서드 실행
	 * 			ProceedingJoinPoint joinPoint를 이용하여 전 후를 구분한다.	
	 *  @Before : 핵심메서드 실행 전 AOP 메서드 실행
	 *  @After :  핵심메서드 실행 후 AOP 메서드 실행
	 *  @AfterReturning : 핵심 메서드 실행 정상 종료 후 AOP 메서드 실행
	 *  @AfterThrowing : 핵심 메서드 실행 비정상 종료 후 AOP 메서드 실행
	 */
	@Around("execution(* controller.User*.loginCheck*(..)) "
									+ "&& args(..,session)")
	public Object userLoginCheck(ProceedingJoinPoint joinPoint,
			HttpSession session) throws Throwable {
		//joinPoint : 실행되는 메서드들의 순서를 관리하는 객체
		//session : Pointcut 메서드의 마지막 매개변수
		User loginUser = (User)session.getAttribute("loginUser");
		if(loginUser == null) { //로그아웃 된 상태
			throw new LoginException	//예외발생
				("[userlogin]로그인 후 거래 하세요","login");
		}
		return joinPoint.proceed(); //다음 메서드로 진행.(joinpoint.proceed기준 실행 전 후를 나눔)
	}
	@Around("execution(* controller.User*.idCheck*(..))"
			+ "&& args(..,id,session)")
	public Object userIdCheck(ProceedingJoinPoint joinPoint,
			String id,HttpSession session) throws Throwable {
		
		//로그인 여부 검증
		User loginUser = (User)session.getAttribute("loginUser");
		if(loginUser == null) { //로그아웃된 상태
			throw new LoginException("[IdCheck]로그인 후 거래하세요","login");
		}//admin이 아니면서,로그인 아이디와 파라미터 id값이 다른 경우
		if(!loginUser.getUserid().equals(id) && !loginUser.equals("admin")) {
			throw new LoginException("[IdCheck]본인만 거래 가능합니다","../item/list");
		}
		return joinPoint.proceed();
	}
	
}