package controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import exception.ItemException;
import logic.Item;
import logic.ShopService;
/*
 * @Component : 객체화 대상이 되는 클래스
 * Controller 기능 : url의 요청시 호출되는 클래스 
 */
@Controller  //@Component + Controller 기능
@RequestMapping("item") //http://localhost:8088/springmvc1/item
public class ItemController {
	@Autowired  //ShopService 객체를 주입 
	private ShopService service;
	//http://localhost:8088/springmvc1/item/list 요청시 호출되는 메서드
	@RequestMapping("list")
	public ModelAndView list() {
		//ModelAndView : 데이터+뷰 정보 저장 
		ModelAndView mav = new ModelAndView();
		//itemList : db의 item 테이블의 모든 데이터를 Item 객체들로 저장 객체
		List<Item> itemList = service.itemList();
		mav.addObject("itemList",itemList); //데이터 저장.
		//뷰의 이름은 기본적으로 요청 url 정보로 설정 : "item/list" 설정
		return mav;	
	}
	//http://localhost:8088/springmvc1/item/detail?id=1
	@RequestMapping("detail") 
	public ModelAndView detail(Integer id) {
		//request.getParameter("id") : id 매개변수명 == 파라미터이름
		ModelAndView mav = new ModelAndView();
		//item : id 에 해당하는 db 레코드 정보를 한개 저장 객체
		Item item = service.getItem(id);
		mav.addObject("item",item); //item 객체에 "item" 이름 설정
		return mav;
	}
	@RequestMapping("create") 
	public ModelAndView create() {
		ModelAndView mav = new ModelAndView();
		mav.addObject(new Item()); //객체만 전달. 
		return mav;
	}
	/*
	 * @Valid : 유효성 검사(입력값 검증)
	 * item : item 객체의 프로퍼티와 요청파라미터의 이름이 같은 것을 
	 *        item 객체에 저장. 파라미터값을 저장하고 있는 객체.
	 * bresult : item 객체에 유효성검증의 결과 저장 객체.       
	 */
	@RequestMapping("register")
	public ModelAndView register(@Valid Item item, 
			BindingResult bresult,
			HttpServletRequest request) {
		//"item/create" : 뷰이름 설정. 
		ModelAndView mav = new ModelAndView("item/create"); 
		if(bresult.hasErrors()) { //유효성 검증에 오류 존재?
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		//item : 요청 파라미터, 업로드된파일의 내용 저장 객체
		//request : 요청객체 
		service.itemCreate(item,request);
		mav.setViewName("redirect:list");
		return mav;
	}
	//http://localhost:8088/springmvc1/item/update?id=1
	/*
	 * RequestMapping : get,post 방식이든 실행
	 * GetMapping : get 방식 호출시 실행
	 * PostMapping : Post 방식 호출시 실행
	 * @GetMapping("update") : item/update 요청정보가 get 방식 호출시 
	 */
	//{"update","delete"} : get 방식 요청 중 update,delete 요청시
	//                      호출되는 메서드가 같음
	//                      update 요청시 : update.jsp
	//                      delete 요청시 : delete.jsp
	@GetMapping({"update","delete"})
	public ModelAndView updateForm(Integer id) {
	  ModelAndView mav = new ModelAndView(); //뷰의 기본값=>url 설정값
		//item : id에 해당하는 데이터를 db에서 읽어서 저장
		Item item = service.getItem(id);
		mav.addObject("item",item);
		return mav;
	}
	/*
	 * 1. 입력값 유효성 검증
	 * 2. db에 내용 수정. 파일 업로드
	 * 3. update 완료시 list 재요청 하기
	 */
	@PostMapping("update")
	public ModelAndView update(@Valid Item item,
			BindingResult bresult, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) { //입력값 오류
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		//db 수정, 파일업로드
		service.itemUpdate(item,request);
		mav.setViewName("redirect:list");
		return mav;
	}	
	//id에 해당하는 상품을 db에서 삭제
	// list로 재요청
	@PostMapping("delete")
	public String delete(Integer id) { //뷰만 리턴
		try {
		    service.itemDelete(id);
		} catch (DataIntegrityViolationException e) {
			throw new ItemException
			("주문된 상품이므로 삭제 불가합니다.","list");
		}
		return "redirect:list";
	}
	
}