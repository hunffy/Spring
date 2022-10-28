package controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.ShopService;
import logic.User;

@Controller
@RequestMapping("user")
public class UserController {
	@Autowired
	private ShopService service;
	
	@GetMapping("*") //* : 그외 모든 get방식 요청
	public ModelAndView getUser() {
		ModelAndView mav = new ModelAndView();
		mav.addObject(new User());
		return mav;
	}
	@PostMapping("join")
	//BindingResult : 오류발생시 오류보관
	public ModelAndView join(@Valid User user, BindingResult bresult) {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) { //입력값 오류 발생.
			mav.getModel().putAll(bresult.getModel()); //데이터의 내용을 오류값들로 담음! 
			
			//전체적인 오류코드 등록(한가지 말고 여러가지 오류를등록할때 사용)
			bresult.reject("error.input.user");
			return mav;
		}
		//오류가없을시 db에 회원정보등록
		try {
			service.userInsert(user); //user객체는 유효성검사를마친 정보를 가짐
			mav.addObject("user",user); //user객체 뷰단으로 전달
			
		//DataIntegrityViolationException : 중복키 오류.
		// 똑같은 아이디가 또 들어왔을때 오류 발생.		같은이름의 userid값이 존재하는경우 발생.
		}catch(DataIntegrityViolationException e) {
			e.printStackTrace();
			bresult.reject("error.duplicate.user"); //error.duplicate.user : message.txt에 나와있음. (오류내용)
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		mav.setViewName("redirect:login");
		return mav;
	}
	
	/* 로그인 - POST 방식 요청
	 * 1. 유효성검증
	 *    User객체에 저장된 파라미터값을 이용하여 유효성검증.
	 * 2. 입력받은 userid,password 로 db에서 해당 정보를 읽기.
	 *     - userid가 없는 경우 
	 *     - password가 틀린 경우
	 *     - 정상적인 사용자인경우 : session에 로그인 정보 등록하기   
	 *     	session.setAttribute("loginUser",user객체)      
	 */

	@PostMapping("login")
	public ModelAndView login(@Valid User user, BindingResult bresult, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		//1. 유효성 검증
		if(bresult.hasErrors()) {
			bresult.reject("error.input.login");
			mav.getModel().putAll(bresult.getModel());//에러가있으면 모델객체에 전부집어넣고
			return mav;
		}
		//userid에 해당하는 User 정보를 db에서 읽어오기
		User dbuser = null;
		try { //아이디가없는 경우 예외처리
		 dbuser = service.getUser(user.getUserid());
		}catch(EmptyResultDataAccessException e) {
			bresult.reject("error.login.id");
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		//정상적인 사용자인경우 세션에 로그인정보 등록
		if(user.getPassword().equals(dbuser.getPassword())) {
			session.setAttribute("loginUser", dbuser);
		//비밀번호 틀린경우 에러메세지 출력
		}else {
			bresult.reject("error.login.password");
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		//정상적인경우 DB에등록된후에 마이페이지로 이동
		mav.setViewName("redirect:mypage?id="+user.getUserid());
		return mav;
	}
	//http://localhost:8088/springmvc1/user/logout
	@RequestMapping("logout")	//loginCheckLogout 핵심메서드: 수행해야될 기본 실행 메서드
	public String loginCheckLogout(HttpSession session) {
		session.invalidate();
		return "redirect:login";
	}
}
