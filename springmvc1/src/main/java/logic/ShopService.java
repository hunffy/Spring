package logic;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dao.ItemDao;
import dao.SaleDao;
import dao.SaleItemDao;
import dao.UserDao;
/*
 * @Component : 해당 클래스를 객체화
 * Service 기능 : Controller 와 Model 사이의 중간 역할의 클래스
 */
@Service //@Component기능 + Service 기능
public class ShopService {
	
	@Autowired //shopservice객체에 ItemDao 객체를 주입해.
	private ItemDao itemDao;
	
	@Autowired
	private UserDao userDao; //shopservice객체에 UserDao 객체를 주입해.
	
	@Autowired
	private SaleDao saleDao;
	
	@Autowired
	private SaleItemDao	saleItemDao;

	public List<Item> itemList() {
		return itemDao.list();
	}

	public Item getItem(Integer id) {
		return itemDao.getItem(id);
	}
	//db에 내용 저장. 파일 업로드 
	//item : 저장할정보를 담은 객체. 입력된 파라미터값 + 업로드된 파일의 내용
	public void itemCreate(@Valid Item item, HttpServletRequest request) {
		//item.getPicture() : 업로드 된 파일의 내용
		if(item.getPicture() != null &&		//업로드된 파일이 존재하는경우
				!item.getPicture().isEmpty()) {
			
			String uploadPath =
			request.getServletContext().getRealPath("/") + "img/";
			
			uploadFileCreate(item.getPicture(),uploadPath); //업로드구현되는 부분. 파일의내용과 업로드되는위치
			item.setPictureUrl	//파일의 이름설정
			(item.getPicture().getOriginalFilename());
		}
		//maxid : item 테이블 중 최대 id값
		int maxid = itemDao.maxId();
		item.setId(maxid+1);
		itemDao.insert(item);
	}

	public void itemUpdate(Item item, HttpServletRequest request) {
		if(item.getPicture() != null && !item.getPicture().isEmpty()) { //파일업로드 되는경우
			
			//파일업로드 될 경로
			String uploadPath = 
			request.getServletContext().getRealPath("/")+"img/";
			
			//파일업로드 : 업로드된 내용을 서버에 파일로 저장
			uploadFileCreate(item.getPicture(), uploadPath);
			
			item.setPictureUrl//파일이름을 DB에 등록하기 위해 설정
			(item.getPicture().getOriginalFilename());
  	    }
		itemDao.update(item);
     }
	
	
	private void uploadFileCreate
					(MultipartFile file, String uploadPath) {
		//uploadPath : 파일이 업로드되는 폴더
		String orgFile = file.getOriginalFilename(); //파일이름을 가져옴
		File fpath = new File(uploadPath);
		
		
		if(!fpath.exists()) fpath.mkdirs(); //업로드 폴더가 없으면 생성
		try {
			//파일의 내용 = > uploadPath + orgFile 로 파일 저장
			file.transferTo
				(new File(uploadPath + orgFile)); //파일업로드
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void itemDelete(Integer id) {
		itemDao.delete(id);
	}

	public void userInsert(User user) {
		userDao.insert(user);
	}

	public void userSearch(@Valid User user) {
		userDao.search(user);
	}

	public User getUser(String userid) {
		return userDao.selectOne(userid);
	}

	public void userUpdate(User user) {
		userDao.update(user);
	}

	public void userDelete(String userid) {
		userDao.delete(userid);
	}

	public void userChgpass(String userid, String pass) {
		userDao.chgpass(userid, pass);
	}

	public String getSearch(User user, String url) {
		return userDao.search(user,url);
	}

	public List<User> userlist() {
		return userDao.list();
	}
	/*
	 * 로그인정보, 장바구니 정보에서 sale,saleitem 테이블에 데이터 저장
	 * 결과를 Sale객체로 저장
	 * 1. sale 테이블의 saleid의 최대값 조회
	 * 2. sale 테이블 saleid의 최대값+1, userid, sysdate 등록
	 * 3. Cart 데이터에서 saleitem 데이터를 추출하여 저장.
	 * 4. Sale 객체에 모든 데이터를 저장
	 * 
	 * 
	 */
	public Sale checkend(User loginUser, Cart cart) {
		//1. sale 테이블의 saleid의 최대값 조회
		int maxid = saleDao.getMaxSaleId();
		
		//2. sale 테이블 saleid의 최대값+1, userid, sysdate 등록
		Sale sale = new Sale();
		sale.setSaleid(maxid+1);
		sale.setUserid(loginUser.getUserid());
		sale.setUser(loginUser);
		saleDao.insert(sale);//sale테이블에 DB저장
		
		//3. Cart 데이터에서 saleitem 데이터를 추출하여 저장. 주문상품에대한 등록
		int seq = 0;
		for(ItemSet is : cart.getItemSetList()) {
			SaleItem saleItem = new SaleItem
					(sale.getSaleid(),++seq,is);
			sale.getItemList().add(saleItem);
			saleItemDao.insert(saleItem); //saleitem 테이블에 저장
		}
		return sale; 
	}

	public List<Sale> salelist(String id) { //id 사용자정보
		//list : sale테이블의 내용을 저장하고있음
		List<Sale> list = saleDao.list(id); // id사용자가 주문한 정보목록 
		for(Sale sa : list) {
			//주문별 주문 상품 조회
			List<SaleItem> saleitemlist = 
							saleItemDao.list(sa.getSaleid());
			//SaleItem 객체에 Item객체 저장하기위한 부분
			for(SaleItem si : saleitemlist) {
				Item item = itemDao.getItem(si.getItemid());
				si.setItem(item); //상품별로 한건한건 읽어서 Item객체를 SaleItem객체에 추가.
			}
			//Sale객체에 SaleItem 목록을 추가.
			sa.setItemList(saleitemlist);
		}
		return list;
	}
		
}
