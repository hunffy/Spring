package logic;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.springframework.web.multipart.MultipartFile;

public class Item {
	private int id;
	//값이 입력이 안된경우 검증
	@NotEmpty(message="상품명을 입력하세요")
	private String name;
	@Min(value=10, message="10원이상 가능합니다.") 		 //  최소값 의미
	@Max(value=100000, message="10만원이하만 가능합니다.")//  최대값 의미
	private int price;
	@NotEmpty(message="상품설명을 입력하세요")
	private String description;
	private String pictureUrl;
	
	//name=picture 태그가 선택한 파일의 내용저장
	//<input type="file" name="picture">
	//picture가 파일의 내용을 담고있다.
	private MultipartFile picture;
	
	//setter,getter, toString
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPictureUrl() {
		return pictureUrl;
	}
	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}
	public MultipartFile getPicture() {
		return picture;
	}
	public void setPicture(MultipartFile picture) {
		this.picture = picture;
	}
	@Override
	public String toString() {
		return "Item [id=" + id + ", name=" + name + ", price=" + price + ", description=" + description
				+ ", pictureUrl=" + pictureUrl + ", picture=" + picture + "]";
	}
	
	
}
