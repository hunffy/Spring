package logic;

import java.util.ArrayList;
import java.util.List;

public class Cart {
	private List<ItemSet> itemSetList = new ArrayList<>();
	
	public List<ItemSet> getItemSetList() {
		return itemSetList;
	}
	public void push(ItemSet itemSet) { 
		//itemSet : 추가될 상품정보
		for(ItemSet is : itemSetList) {
			//is : 이미 추가된 상품정보
			if(itemSet.getItem().getId() == is.getItem().getId()) {
				is.setQuantity(is.getQuantity()+itemSet.getQuantity());
				return;
			}
		}
		
		itemSetList.add(itemSet); //장바구니 목록에 추가
	}

	
	
	public int getTotal() { //total get 프로퍼티
		int sum = 0;
		for(ItemSet is : itemSetList) {
			sum += is.getItem().getPrice() * is.getQuantity();
 		}
		return sum;
	}
}
