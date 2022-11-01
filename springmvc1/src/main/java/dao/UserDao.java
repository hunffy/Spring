package dao;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import logic.User;

@Repository //객체화
public class UserDao {
	private NamedParameterJdbcTemplate template;
	private Map<String,Object> param = new HashMap<>();
	private RowMapper<User> mapper = new BeanPropertyRowMapper<>(User.class);
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		template = new NamedParameterJdbcTemplate(dataSource);
	}
	public void insert(User user) {
		SqlParameterSource param =
					new BeanPropertySqlParameterSource(user);
		String sql = "insert into useraccount"
				+ " (userid, password, username, phoneno, postcode,"
				+ " address, email, birthday)"
				+ " values(:userid, :password, :username, :phoneno, :postcode,"
				+ " :address, :email, :birthday)";
					template.update(sql, param);
	}
	public void search(@Valid User user) {
		SqlParameterSource param =
				new BeanPropertySqlParameterSource(user);
		String sql = "select * from useraccount where userid=:userid";
	}
	public User selectOne(String userid) {
		param.clear();
		param.put("userid", userid);
		//조회되는 결과가 없는경우: EmptyResultDataAccessException 예외 발생.
		return template.queryForObject("select * from useraccount where userid=:userid", param, mapper);
	}
	public void update(User user) {
		SqlParameterSource param =
				new BeanPropertySqlParameterSource(user);
		String sql = "update useraccount set username=:username,"
				+ "birthday=:birthday, phoneno=:phoneno, postcode=:postcode,"
				+ "address=:address, email=:email where userid=:userid";
		template.update(sql, param);
	}
	public void delete(String userid) {
		param.clear();
		param.put("userid", userid);
		template.update("delete from useraccount where userid=:userid",param);
	}
	public void chgpass(String userid, String pass) {
		param.clear();
		param.put("userid", userid);
		param.put("password", pass);
		template.update
		("update useraccount set password=:password where userid=:userid", param);
	}
}