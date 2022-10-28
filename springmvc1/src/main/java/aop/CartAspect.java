package aop;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import exception.LoginException;
import logic.Cart;
import logic.User;

@Component	// 객체화를 실행해
@Aspect		// AOP 기능의 클래스
public class CartAspect {
	@Before("execution(* controller.Cart*.check*(..)) "
			+ "&& args(..,session)")
	public void cartCheck(HttpSession session)
		throws Throwable {
		User loginUser = (User)session.getAttribute("loginUser");
		if(loginUser == null) {
			throw new LoginException
			("회원만 주문 가능합니다. 로그인 하세요","../user/login");
		}
		Cart cart = (Cart)session.getAttribute("CART");
		if(cart == null || cart.getItemSetList().size() == 0) {
			throw new LoginException
			("장바구니에 주문 상품이 없습니다.","../item/list");
		}
	}
}
