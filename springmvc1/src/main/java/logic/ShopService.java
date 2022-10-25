package logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.ItemDao;
/*
 * @Component : 해당 클래스를 객체화
 * Service 기능 : Controller 와 Model 사이의 중간 역할의 클래스
 */
@Service //@Component + Service 기능
public class ShopService {
	
	@Autowired //ItemDao 객체를 주입해.
	private ItemDao itemDao;

	public List<Item> itemList() {
		return itemDao.list();
	}

	public Item getItem(Integer id) {
		return itemDao.getItem(id);
	}
}
