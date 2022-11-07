package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dao.mapper.SaleItemMapper;
import logic.SaleItem;

@Repository
public class SaleItemDao {
	@Autowired
	private SqlSessionTemplate template;
	
	public void insert(SaleItem saleitem) {
		template.getMapper(SaleItemMapper.class).insert(saleitem);
	}
	public List<SaleItem> list(int saleid) {
	  return template.getMapper(SaleItemMapper.class).list(saleid);
	}
}