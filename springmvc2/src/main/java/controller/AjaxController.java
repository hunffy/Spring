package controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import logic.ShopService;
 
/*
 * @Controller : @Component + Controller 기능
 *    메서드 리턴타입 : String => 뷰의 이름 지정
 *    메서드 리턴타입 : ModelAndView => 뷰의 이름 지정 + 데이터
 *     
 * @RestController : @Component + Controller 기능 + 
 *                   클라이언트에 값 전달시 뷰 없이 바로데이터로 전달 
 *    메서드 리턴타입 : String => 브라우저로 전달할 데이터 값.
 *    메서드 리턴타입 : Object => 브라우저로 전달할 데이터 값.
 *    spring 4.0 이후에 추가된 컨트롤러
 *    @ResponseBody 기능 : 메서드의 어노테이션으로 설정. 
 */
@RestController
@RequestMapping("ajax")
public class AjaxController {
	@Autowired
	ShopService service;
	
	@RequestMapping("select")
	public List<String> select
	   (String si,String gu,HttpServletRequest request) {
		BufferedReader fr = null;
		String path = request.getServletContext().getRealPath("/")
				+ "file/sido.txt";
		try {
			fr = new BufferedReader(new FileReader(path));
		} catch(Exception e) {
			e.printStackTrace();
		}
		//Set : 중복불가.
		//LinkedHashSet : 중복불가. 순서 유지 가능
		Set<String> set = new LinkedHashSet<>();
		String data = null;
		//si,gu 파라미터값이 없는 경우
		if(si==null && gu == null) {
			try {
				//readLine() : 한줄씩 읽기
				while((data=fr.readLine()) != null) {
					//\\s+ : \\s : 공백, +:1개이상
					//       공백한개이상으로 문자를 분리하여 배열 저장 
					String[] arr = data.split("\\s+");
					if(arr.length >= 3) set.add(arr[0].trim());
				} 
			}catch(IOException e) {
				e.printStackTrace();
			}
		} else if(gu == null) { //si 파라미터 존재 
		   si = si.trim();
		   try {
			while ((data = fr.readLine()) != null) {
				 String[] arr = data.split("\\s+");
		  		 if(arr.length >= 3 && arr[0].equals(si) && 
						 !arr[1].contains(arr[0]) ) {
					 set.add(arr[1].trim()); //구정보 저장
				 }
			   }
		  } catch (IOException e) {
			e.printStackTrace();
		  }
		} else { //si 파라미터,gu 파라미터 존재 
		  si = si.trim();
		  gu = gu.trim();
		  try {
		    while ((data = fr.readLine()) != null) {
			  String[] arr = data.split("\\s+");
	          if(arr.length >= 3 && arr[0].equals(si) &&
		    	 arr[1].equals(gu) && !arr[0].equals(arr[1]) &&
		   		 !arr[2].contains(arr[1])) {
		          	 if(arr.length > 3 ) {
		          		if(arr[3].contains(arr[1])) continue;
		          		arr[2] += " " + arr[3];
		          	 }
		          	 set.add(arr[2].trim()); //동정보 저장
		      }
		    }
		  } catch (IOException e) {
			e.printStackTrace();
		  }
	  }
	  List<String> list = new ArrayList<>(set);
	  return list; //배열로 브라우저(ajax객체)에 전달
	}
	//produces : 클라이언트에 전달되는 데이터의 특징 설정.
	// text/plain : 순수문자열.
	// charset=utf-8 : 문자열의 인코딩방식 전달
	@RequestMapping(value="select2",
			produces="text/plain; charset=utf-8")
	public String select2
	   (String si,String gu,HttpServletRequest request) {
		BufferedReader fr = null;
		String path = request.getServletContext().getRealPath("/")
				+ "file/sido.txt";
		try {
			fr = new BufferedReader(new FileReader(path));
		} catch(Exception e) {
			e.printStackTrace();
		}
		Set<String> set = new LinkedHashSet<>();
		String data = null;
		if(si==null && gu == null) {
			try {
				while((data=fr.readLine()) != null) {
					String[] arr = data.split("\\s+");
					if(arr.length >= 3) set.add(arr[0].trim());
				} 
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
		List<String> list = new ArrayList<>(set);
		return list.toString(); //문자열로 브라우저(ajax객체)에 전달
	}

}