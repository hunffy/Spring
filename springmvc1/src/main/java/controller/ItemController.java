package controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.Item;
import logic.ShopService;
/*
 * @Component : 객체화 대상이 되는 클래스
 * Controller 기능 : url의 요청시 호출되는 클래스
 */
@Controller //1.요청이들어오면 나를 호출해 .@Component를 포함한다. + Controller 기능
@RequestMapping("item")	// http://localhost:8088/springmvc1/item 요청들어오면 실행
public class ItemController {
	@Autowired //컨네이너에 ShopService 객체를 주입해.
	private ShopService service;
	//http://localhost:8088/springmvc1/item 요청시 호출되는 메서드
	@RequestMapping("list")
	public ModelAndView list() {
		//1.ModelAndView: 데이터+뷰 정보를 저장하고있는 객체생성
		ModelAndView mav = new ModelAndView();
		
		//itemList : DB의 item 테이블의 모든 데이터를  Item 객체들로 저장하고있는 객체
		List<Item> itemList = service.itemList();
		mav.addObject("itemList", itemList); //데이터를 저장.
		
		//뷰의 이름은 기본적으로 요청 url의 정보로 설정 : "item/list" 설정
		return mav;
	}
	//http://localhost:8088/springmvc1/item/deatil?id=1
	@RequestMapping("detail")
	public ModelAndView detail(Integer id) {
		//request.getParameter("id") = id 매개변수명 == 파라미터이름
		ModelAndView mav = new ModelAndView();
		
		//item : id에 해당하는 DB의 레코드 정보를 한개 저장하고있는 객체
		Item item = service.getItem(id);
		mav.addObject("item",item); //item 객체에 "item" 이름을 설정해준것.
		return mav;
	}
	@RequestMapping("create")
	public ModelAndView create() {
		ModelAndView mav = new ModelAndView();
		mav.addObject(new Item()); //객체만 전달 , 유효성검증을위해서
		return mav;
	}
	/*
	 * @Valid : 유효성 검사(입력값 검증) ->입력되었는지 안되었는지 
	 * item : item 객체의 프로퍼티와 요청파라미터의 이름이 같은 것을
	 * 		  item 객체에 저장해줌. 입력된 파라미터값들을 저장하고 있는 객체.
	 * bresult : item 객체에 유효성검증의 결과 저장한 객체
	 * 
	 */
	@RequestMapping("register")
	public ModelAndView register(@Valid Item item, 
			BindingResult bresult,
			HttpServletRequest request) {
		
		//"item/create : 뷰 이름 설정
		ModelAndView mav = new ModelAndView("item/create");
		if(bresult.hasErrors()) {	//유효성 검증에 오류가 존재해?
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		//유효성검사 통과
		//item 객체 : 요청파라미터 정보, 업로드 된 파일의 내용 저장 객체
		//request  : 요청객체 , path를 가져오기위해 사용함.
		service.itemCreate(item,request);
		mav.setViewName("redirect:list"); //유효성검증이 완료되면
		return mav;
	}
	//http://localhost:8088/springmvc1/item/update?id=1
	/*
	 * RequestMapping : 지정된 url이 맞는다면 get,post 방식 상관없음
	 * GetMapping  :  get 방식 호출시 실행
	 * PostMapping : Post방식 호출시 실행
	 * @GetMapping("update") : item/update 요청정보가 get 방식 호출시
	 */
	
	//{"update","delete"} : get방식 요청 중 update,delete 요청시
	//						호출되는 메서드가 같음.
	//						update 요청시 : update.jsp
	//						delete 요청시 : delete.jsp
	@GetMapping({"update","delete"}) //1. update가 요청이오면 실행
	public ModelAndView updateForm(Integer id) { //2. id값을 가져와야하기때문에 integer id
		ModelAndView mav = new ModelAndView(); //뷰의 기본값은 url 설정 값.
		
		//item: id에 해당하는 데이터를 DB에서 읽어서 저장
		Item item = service.getItem(id);
		mav.addObject("item",item); //5. mav객체에 "item"이라는 이름에 정보를담은 item 대입
		return mav;//6. mav객체 리턴
	}
	/*
	 * 1. 입력값 유효성 검증
	 * 2. db에 내용 수정. 파일 업로드 
	 * 3. update 완료시 list 재요청
	 */
	@PostMapping("update")
	public ModelAndView update(@Valid Item item, BindingResult bresult, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) {//입력값검증에서 오류가발생한경우
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		//db 내용수정, 파일업로드
		service.itemUpdate(item,request);
		mav.setViewName("redirect:list");
		return mav;
	}
	@PostMapping("delete")
	public String delete(Integer id) { //뷰만 리턴
		service.itemDelete(id);
		return "redirect:list";
	}
 }
