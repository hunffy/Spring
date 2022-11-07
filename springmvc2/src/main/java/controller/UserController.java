package controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import exception.LoginException;
import logic.Sale;
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
	public ModelAndView join
	             (@Valid User user,BindingResult bresult) {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) { //입력값 오류 발생
			mav.getModel().putAll(bresult.getModel());
			//전체적인 오류코드 등록
			bresult.reject("error.input.user");
			return mav;
		}
		//db에 회원정보 등록
		try {
			service.userInsert(user);
			mav.addObject("user",user);
		//DataIntegrityViolationException : 중복키 오류.
		//                       같은 이름의 userid값이존재	
		} catch(DataIntegrityViolationException e) {
			e.printStackTrace();
			bresult.reject("error.duplicate.user");
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
	 *       session.setAttribute("loginUser",user객체)       
	 */	
	@PostMapping("login")
	public ModelAndView login(@Valid User user, BindingResult bresult,
			HttpSession session) {
		ModelAndView mav = new ModelAndView();
		//1. 유효성검증
		if(bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
			bresult.reject("error.input.login");
			return mav;
		}
		//userid에 해당하는 User 정보를 db에서 읽어 오기
		User dbuser = null;
		try { //아이디가 없는 경우 예외처리
		   dbuser = service.getUser(user.getUserid());
		} catch(EmptyResultDataAccessException e) {
			bresult.reject("error.login.id");
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		if(user.getPassword().equals(dbuser.getPassword())) {
			session.setAttribute("loginUser", dbuser);
		} else {
			bresult.reject("error.login.password");
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		mav.setViewName("redirect:mypage?id="+user.getUserid());
		return mav;
	}
	//http://localhost:8088/springmvc1/user/logout
	@RequestMapping("logout")  //핵심메서드 : 기본 실행 메서드
	public String loginCheckLogout(HttpSession session) {
		session.invalidate();
		return "redirect:login";
	}
	/*
	 * AOP 설정하기 : 
	 *   UserLoginAspect 클래스의 userIdCheck 메서드로 구현하기.
	 * 1.pointcut : UserController 클래스의 idCheck로 시작하는 메서드이고, 
	 *              마지막 매개변수의 id,session인 경우 
	 * 2. 로그인 여부 검증
	 *   - 로그인이 안된경우 로그인 후 거래하세요. 메세지 출력. login페이지 호출
	 * 3. admin이 아니면서, 로그인 아이디와 파라미터 id값이 다른 경우
	 *   - 본인만 거래 가능합니다. 메세지 출력. item/list 페이지 호출              
	 */
	@RequestMapping("mypage")
	public ModelAndView idCheckMypage(String id,HttpSession session) {
		ModelAndView mav = new ModelAndView();
		//회원정보를 조회하여 user 이름으로 뷰로 전달
		User user = service.getUser(id);
		//주문정보를 조회하여 뷰에 전달
		List<Sale> salelist = service.salelist(id);
		mav.addObject("user", user);
		mav.addObject("salelist", salelist);  //주문목록
		return mav;
	}
	@GetMapping({"update","delete"})
	public ModelAndView idCheckUser(String id,HttpSession session) {
		ModelAndView mav = new ModelAndView();
		User user = service.getUser(id);
		mav.addObject("user",user);
		return mav;
	}
	@PostMapping("update")
	public ModelAndView idCheckUpdate
       (@Valid User user, BindingResult bresult,
    		            String userid,HttpSession session) {
		ModelAndView mav = new ModelAndView();
		//유효성 검증
		if(bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
			bresult.reject("error.update.user");
			return mav;
		}
		//비밀번호 검증 : 로그인된 정보의 비밀번호로 검증.
		User loginUser = (User)session.getAttribute("loginUser");
		if(!loginUser.getPassword().equals(user.getPassword())) {
			mav.getModel().putAll(bresult.getModel());
			bresult.reject("error.login.password");
			return mav;
		}
		//비밀번호 일치인 경우: db에 내용 수정
		try {
			service.userUpdate(user);
			//session에 로그인 정보 수정
			session.setAttribute("loginUser", user);
			mav.setViewName
			       ("redirect:mypage?id="+user.getUserid());
		} catch (Exception e) {
			e.printStackTrace();
			throw new LoginException
			("고객 정보 수정 실패","update?id="+user.getUserid());
		}
		return mav;
	}
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
	
	@PostMapping("delete")
	public ModelAndView  idCheckdelete
	       (String password, String userid,HttpSession session) {
		ModelAndView mav = new ModelAndView();
		//관리자인 경우 탈퇴 불가
		if(userid.equals("admin"))
			throw new LoginException
			  ("관리자 탈퇴는 불가합니다.", "mypage?id="+userid);
		//로그인된 비밀번호 검증
		User loginUser = (User)session.getAttribute("loginUser");
		//로그인된 비밀번호 검증 : 불일치
		if(!password.equals(loginUser.getPassword())) {
			throw new LoginException
			     ("비밀번호를 확인하세요.", "delete?id="+userid);
		}
		//로그인된 비밀번호 검증 : 일치
		try {
			service.userDelete(userid); //db에서 해당 사용자정보 삭제
		} catch(Exception e) {
			e.printStackTrace();
			throw new LoginException
			   ("탈퇴시 오류발생.", "delete?id="+userid);
		}
		//탈퇴 성공
	    //관리자 회원 강제 탈퇴
		if(loginUser.getUserid().equals("admin")) {
			mav.setViewName("redirect:../admin/list");
		} else {  //본인 탈퇴  
			mav.setViewName("redirect:login");
			session.invalidate();
		}	
		return mav;
	}
	@GetMapping("password")
	public String loginCheckPassword(HttpSession session) {
		return null;
	}
	/*
	 * 1. 로그인 검증
	 * 2. 파라미터값을 Map 객체 저장 
	 *    @RequestParam : 요청파라미터를 Map객체로 저장하도록하는 어노테이션
	 *    req.put("password",password의 파라미터값)
	 *    req.put("chapass",chgpass의 파라미터값)
	 *    req.put("chapass2",chgpass2의 파라미터값)
	 * 3. 현재비밀번호와 입력된 비밀번호 검증
	 *    불일치 : 오류 메세지 출력. password 페이지 이동
	 * 4. 일치 : db 수정
	 * 5. 성공 : 로그인정보 변경, mypage 페이지 이동
	 *    실패 : 오류 메세지 출력. password 페이지 이동   
	 */
	@PostMapping("password") 
	public String loginCheckPasswordRtn(
	 @RequestParam Map<String,String>req,HttpSession session) {
		System.out.println(req);
		//비밀번호 검증 
		User loginUser = (User)session.getAttribute("loginUser");
		//req.get("password") : 입력된 현재 비밀번호 
		//loginUser.getPassword() : 등록된 비밀번호
		if(!req.get("password").equals(loginUser.getPassword())) {
		  throw new LoginException("비밀번호 오류 입니다.","password");
		}
		try { //비밀번호 일치
			//loginUser.getUserid() : 로그인 사용자아이디
			//req.get("chgpass") : 입력된 변경할 비밀번호
			service.userChgpass
			   (loginUser.getUserid(),req.get("chgpass"));
			//로그인 정보 변경
			loginUser.setPassword(req.get("chgpass"));
		} catch(Exception e) {
			  throw new LoginException
			  ("비밀번호 수정시 db 오류 입니다.","password");
		}
		return "redirect:mypage?id="+loginUser.getUserid();
	}
	/*
	 * "{url}search" : *search 인 요청인 경우 호출되는 메서드
	 * @PathVariable String url : {url}값을 매개변수 전달.
	 * 
	 * http://localhost:8088/springmvc1/user/idsearch
	 * url : id 값 저장
	 * 
	 * http://localhost:8088/springmvc1/user/pwsearch
	 * url : pw 값 저장
	 */
	@PostMapping("{url}search")
	public ModelAndView search(User user, BindingResult bresult,
			@PathVariable String url) {
		ModelAndView mav = new ModelAndView();
		String code = "error.userid.search"; //아이디를 찾을 수 없습니다.
		String title = "아이디";
		if(user.getEmail() == null || user.getEmail().equals("")){
			//rejectValue : 프로퍼티별로 오류 정보 저장.
			// <form:errors path="email" /> 출력됨
			//오류코드 : error.required.email 설정됨
			bresult.rejectValue("email", "error.required"); 
		}
		if(user.getPhoneno() == null || 
				                  user.getPhoneno().equals("")) {
			// <form:errors path="phoneno" /> 출력됨
			//오류코드 : error.required.phoneno 설정됨
			bresult.rejectValue("phoneno", "error.required");
		}
		if(url.equals("pw")) { 
			title="비밀번호";
			code = "error.password.search"; 
			if(user.getUserid() == null || 
					                user.getUserid().equals("")) {
				//<form:errors path="userid" /> 출력됨
				//오류코드 : error.required.userid 설정됨
				bresult.rejectValue("userid", "error.required");
			}
		}
		if(bresult.hasErrors()) { 
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		//정상적인 입력인 경우
		String result = null;
		//result : 사용자id,또는 비밀번호 저장
	    result = service.getSearch(user,url);
	    if(result == null) {
			bresult.reject(code); //global 오류 영역 저장 
			mav.getModel().putAll(bresult.getModel());
			return mav;
	    }
//EmptyResultDataAccessException : mybatis에서 발생 안됨. 결과가 없는 경우
		mav.addObject("result",result);
		mav.addObject("title",title);
		// /WEB-INF/view/user/search.jsp
		mav.setViewName("user/search"); 
		return mav;
	}
}