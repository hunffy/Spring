package controller;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import exception.BoardException;
import logic.Board;
import logic.ShopService;

@Controller
@RequestMapping("board")
public class BoardController {

	@Autowired
	private ShopService service;
	
	@RequestMapping("list")
	public ModelAndView list
	    (Integer pageNum, String boardid,HttpSession session) {
		ModelAndView mav = new ModelAndView();

		if(pageNum == null || pageNum.toString().equals("")) {
			   pageNum = 1;  //pageNum 파라미터가 없는 경우 1로 설정
		}
		if(boardid == null || boardid.equals("")) {
			boardid = "1"; //boardid 파라미터가 없는 경우 1로 설정
		}
		session.setAttribute("boardid", boardid); //게시판 종류 세션에 등록
		String boardName=null; //게시판이름 저장
		switch(boardid) {
			case "1" : boardName = "공지사항"; break;
			case "2" : boardName = "자유게시판"; break;
			case "3" : boardName = "QNA"; break;
		}
		int limit = 10; //한페이지에 출력될 게시물 갯수. 한페이지10개씩 출력
		//게시판종류별 등록된 게시글 갯수
		int listcount = service.boardcount(boardid); 
		//boardlist : 한페이지에 출력될 게시물 목록
		List<Board> boardlist = service.boardlist
				(pageNum,limit,boardid);

		//페이징처리를 위한 데이터
		//최대 페이지 
		/* listcount   maxpage
		 *    1           1
		 *    (int)((double)1/10 + 0.95) => 0.1+0.95 = 1.05 => 1
		 *   10           1
		 *    (int)((double)10/10 + 0.95) => 1.0+0.95 = 1.95 =>1
		 *   11           2 
		 *    (int)((double)11/10 + 0.95) => 1.1+0.95 = 2.05 =>2
		 *   500          50 
		 *    (int)((double)500/10 + 0.95) => 50.0+0.95 = 50.95 =>50
		 *   501          51 
		 *    (int)((double)501/10 + 0.95) => 50.1+0.95 = 51.05 =>51
		 */
		int maxpage = (int)((double)listcount/limit + 0.95);
		//화면에 표시될 페이지의 시작번호
		/*   pageNum   startpage
		 *      1          1
		 *      (int)((1/10.0 + 0.9) - 1) * 10 + 1 =>
		 *            ((0.1+0.9)-1) * 10 + 1 => 1
		 *      5          1
		 *      (int)((5/10.0 + 0.9) - 1) * 10 + 1 =>
		 *            ((0.5+0.9)-1) * 10 + 1 => 1
		 *      10         1
		 *      (int)((10/10.0 + 0.9) - 1) * 10 + 1 =>
		 *            ((1.0+0.9)-1) * 10 + 1 => 1
		 *      11         11
		 *      (int)((11/10.0 + 0.9) - 1) * 10 + 1 =>
		 *            ((1.1+0.9)-1) * 10 + 1 => 11
		 *      20         11
		 *      (int)((20/10.0 + 0.9) - 1) * 10 + 1 =>
		 *            ((2.0+0.9)-1) * 10 + 1 => 11
		 */
		int startpage = (int)((pageNum/10.0 + 0.9) - 1) * 10 + 1;
		//화면에 표시될 페이지의 끝번호, 시작번호부터 10개
		int endpage = startpage + 9;
		//endpage는 maxpage를 넘으면 안됨.
		if(endpage > maxpage) endpage = maxpage;
		//화면에 표시될 순차적인 게시물번호
		/*
		 * listcount pageNum boardno
		 *    1         1        1
		 *    1 - (1 - 1) * 10 =>1
		 *    11        1        11 
		 *    11 - (1 - 1) * 10 =>11
		 *    11        2        1 
		 *    11 - (2 - 1) * 10 =>1
		 */
		int boardno = listcount - (pageNum - 1) * limit;
		//list.jsp 뷰에 전달할 데이터 설정
		mav.addObject("boardid",boardid);  
		mav.addObject("boardName", boardName); 
		mav.addObject("pageNum", pageNum); 
		mav.addObject("maxpage", maxpage); 
		mav.addObject("startpage", startpage);
		mav.addObject("endpage", endpage); 
		mav.addObject("listcount", listcount);
		mav.addObject("boardlist", boardlist);
		mav.addObject("boardno", boardno); 
		return mav;		
	}
	@GetMapping("write")
	public ModelAndView getBoard(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		String boardid = (String)session.getAttribute("boardid");
		if(boardid == null) boardid="1";
		String boardName = null;
		switch(boardid) {
		case "1" :  boardName="공지사항"; break;
		case "2" :  boardName="자유게시판"; break;
		case "3" :  boardName="QNA"; break;
		}
		mav.addObject("board",new Board()); 
		mav.addObject("boardName",boardName); 		
		return mav;
	}
	/* Post 방식 : write
	 * 1. 유효성 검증
	 * 2. 파일 업로드 
	 *    db의 board테이블에 내용 저장
	 * 3. 등록 성공 : list로 
	 *    등록 실패 : write로     
	 */
	@PostMapping("write")
	public ModelAndView write(@Valid Board board,
			BindingResult bresult,HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		String boardid = 
		   (String)request.getSession().getAttribute("boardid");
		if(boardid==null)
			boardid = "1";
		board.setBoardid(boardid);
		board.setIp(request.getRemoteAddr());
		service.boardwrite(board,request);
		mav.setViewName("redirect:list?boardid="+boardid);
		return mav;
	}
	@GetMapping("detail")
	public ModelAndView detail(Integer num,HttpSession session) {
		ModelAndView mav = new ModelAndView();
		String boardid = (String)session.getAttribute("boardid");
		Board board = service.getBoard(num); //num 게시판 내용 조회 
		service.readcntadd(num);   //조회수 1 증가
		mav.addObject("board",board);
		if(boardid == null || boardid.equals("1"))
			mav.addObject("boardName","공지시항");
		else if(boardid.equals("2"))
			mav.addObject("boardName","자유게시판");
		else if(boardid.equals("3"))
			mav.addObject("boardName","QNA");
		return mav;
	}
	@RequestMapping("imgupload")
	public String imgupload(MultipartFile upload, 
			String CKEditorFuncNum, HttpServletRequest request,
			Model model) {
		/*
		 * upload : CKEditor모듈에서 업로드 이미지의 이름을 지정 
		 *          업로드된 이미지의 내용 저장 
		 * CKEditorFuncNum : CKEditor모듈에서 이름 지정. 리턴이 필요한 값
		 * model : 뷰에 전달할 데이터의 집합. 
		 *         뷰의 이름은 imgupload 함수의 리턴값
		 */
		//path : 서버의 업로드되는 폴더 
		String path = request.getServletContext().getRealPath("/")
				+"board/imgfile";
		File f = new File(path);
		if(!f.exists()) f.mkdirs(); //폴더 생성
		if(!upload.isEmpty()) { //업로드된 이미지가 존재.
		  File file = new File(path,upload.getOriginalFilename());
		  try {
			  upload.transferTo(file); //이미지 업로드됨
		  }catch (Exception e) {
			  e.printStackTrace();
		  }
		}
		//request.getContextPath() : /springmvc2. 프로젝트명
		String fileName = request.getContextPath()
				        + "/board/imgfile/" 
	                    + upload.getOriginalFilename();		
		model.addAttribute("fileName",fileName);
		model.addAttribute("CKEditorFuncNum",CKEditorFuncNum);
		return "ckedit"; //뷰. /WEB-INF/view/ckedit.jsp
	}
	
	@GetMapping({"update","reply","delete"})
	public ModelAndView getBoard(Integer num,HttpSession session) {
		ModelAndView mav = new ModelAndView();
		String boardid = (String)session.getAttribute("boardid");
		Board board = service.getBoard(num); //num 게시판 내용 조회 
		mav.addObject("board",board);
		if(boardid == null || boardid.equals("1"))
			mav.addObject("boardName","공지시항");
		else if(boardid.equals("2"))
			mav.addObject("boardName","자유게시판");
		else if(boardid.equals("3"))
			mav.addObject("boardName","QNA");
		return mav;
	}
	@PostMapping("update")
	public ModelAndView update
	        (@Valid Board board,BindingResult bresult,
			   HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		//비밀번호 검증
		Board dbBoard =	service.getBoard(board.getNum());
		//board.getPass() : 입력된 비밀번호
		//dbBoard.getPass() : db테이블의 비밀번호. 저장된 비밀번호
		if(!board.getPass().equals(dbBoard.getPass())) {  
			throw new BoardException
			 ("비밀번호가 틀립니다.","update?num="+board.getNum());
		}
		//비밀번호가 일치하는 경우. 
		try {
			board.setFileurl(request.getParameter("file2"));
			//파일업로드,db에 수정
			service.boardUpdate(board, request);
			mav.setViewName("redirect:detail?num="+board.getNum());
		} catch (Exception e) { //수정시 오류 발생
			e.printStackTrace();
			throw new BoardException
		  ("게시글 수정을 실패 했습니다.","update?num="+board.getNum());
		}
		return mav;
	}	
/*
 * 1. 유효성 검사하기-파라미터값 저장. 
 *     - 원글정보 : num,grp,grplevel,grpstep,boardid
 *     - 답글정보 : writer,pass,subject,content
 * 2. db에 insert => service.boardReply()
 *     - 원글의 grpstep 보다 큰 이미 등록된 답글의 grpstep 값을 +1 
 *       => boardDao.grpStepAdd()
 *     - db에 insert  => boardDao.reply()
 *       num : maxNum() + 1
 *       grp : 원글과 동일
 *       grplevel : 원글의 grplevel + 1    
 *       grpstep : 원글의 grpstep + 1
 * 3. 등록 성공 : list로 페이지 이동
 *    등록 실패 : "답변 등록시 오류 발생" reply 페이지 이동           
 */	
	@PostMapping("reply")
	public ModelAndView reply(@Valid Board board, 
			BindingResult bresult,
			HttpServletRequest request) {
    	ModelAndView mav = new ModelAndView();
    	if(bresult.hasErrors()) {
    		Board dbboard = service.getBoard(board.getNum());
    		Map<String,Object> map = bresult.getModel();
    		Board b = (Board)map.get("board");
    		b.setSubject(dbboard.getSubject());//원글의 제목으로 변경
			mav.getModel().putAll(bresult.getModel());
			return mav;
    	}
    	try {
    	   board.setIp(request.getRemoteAddr());	
    	   service.boardReply(board);
    	   mav.setViewName
    	   ("redirect:list?boardid="+board.getBoardid());
    	} catch(Exception e) {
    		e.printStackTrace();
    		throw new BoardException("답변등록시 오류 발생",
    				"reply?num="+board.getNum());
    	}
	    return mav;
	}
	/*
	 * 1. num, pass 파라미터 저장
	 * 2. num의 게시물 읽기=> 비밀번호 검증
	 *    비밀번호 오류 : error.login.password 입력 => 뷰에 전달.
	 * 3. db에서 num값의 게시물을 삭제.
	 *    삭제 성공 : list 페이지 이동
	 *    삭제 실패 : delete 페이지로 이동   
	 */
	@PostMapping("delete")
	public ModelAndView delete(Board board,BindingResult bresult) {
		ModelAndView mav = new ModelAndView();
		Board dbBoard = service.getBoard(board.getNum());
		//비밀번호 입력이 안된경우
		if(board.getPass() == null || 
				    board.getPass().trim().equals("")) {
			bresult.reject("error.required.password");
			return mav;
		}
		//비밀번호 오류
		if(!board.getPass().equals(dbBoard.getPass())) {
			bresult.reject("error.login.password");
			return mav;
		}
		//db에서 삭제
		try {
		   service.boardDelete(board.getNum());
		   mav.setViewName
		      ("redirect:list?boardid="+dbBoard.getBoardid());
		} catch (Exception e) {
		   e.printStackTrace();	
		   throw new BoardException
		   ("게시물 삭제시 오류 발생","delete?num="+board.getNum());
		}
		return mav;
	}
}