package dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import logic.Sale;

public interface SaleMapper {

	@Select("select nvl(max(saleid),0) from sale")
	int getMaxSaleId();

	@Insert("insert into sale (saleid, userid, saledate)"
			+ " values (#{saleid},#{userid},sysdate)")
	void insert(Sale sale);

	@Select("select * from sale where userid=#{userid}"
			+ " order by saleid desc")
	List<Sale> list(Map<String, Object> param);
}