package dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import logic.SaleItem;

public interface SaleItemMapper {

	@Insert("insert into saleitem "
		+ " (saleid,seq,itemid,quantity,price) "
		+ " values(#{saleid},#{seq},#{itemid},#{quantity},#{item.price})")
	void insert(SaleItem saleitem);

	@Select("select * from saleitem where saleid = #{saleid}")
	List<SaleItem> list(int saleid);

}