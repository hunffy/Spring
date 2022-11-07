package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dao.mapper.BoardMapper;
import logic.Board;

@Repository //객체생성
public class BoardDao {
	@Autowired
	private SqlSessionTemplate template;
	private Map<String, Object> param = new HashMap<>();
	private Class<BoardMapper> cls = BoardMapper.class;
	
	public int count(String boardid) {
		return template.getMapper(cls).count(boardid);
	}

	public List<Board> list(Integer pageNum, int limit, String boardid) {
		param.clear();
		int startrow = (pageNum - 1) * limit + 1; //1페이지면 10 2페이지면 11부터
		int endrow = startrow + limit - 1; //startrow에서 limit만큼 
		param.put("startrow", startrow);
		param.put("endrow", endrow);
		param.put("boardid", boardid);
		return template.getMapper(cls).list(param);
	}

}
