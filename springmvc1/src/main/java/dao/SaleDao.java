package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import logic.Sale;

@Repository
public class SaleDao {
	private NamedParameterJdbcTemplate template;
	private Map<String,Object> param = new HashMap<>();
	private RowMapper<Sale> mapper = new BeanPropertyRowMapper<>(Sale.class);
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		template = new NamedParameterJdbcTemplate(dataSource);
	}

	public int getMaxSaleId() {
		
		return template.queryForObject
				("select nvl(max(saleid),0) from sale",param,Integer.class);
	}

	public void insert(Sale sale) {
		String sql = "insert into sale (saleid, userid, saledate)"
				+" values (:saleid,:userid,sysdate)";
		SqlParameterSource param =
				new BeanPropertySqlParameterSource(sale);
		template.update(sql, param);
	}

	public List<Sale> list(String id) {
		//id: 사용자아이디 
		String sql = "select * from sale where userid=:userid"
				+ " order by saleid desc";
		param.clear();
		param.put("userid", id);
		return template.query(sql, param,mapper);
	}
	
}
