package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dao.mapper.BoardMapper;
import logic.Board;

@Repository
public class BoardDao {
	@Autowired
	private SqlSessionTemplate template;
	private Map<String, Object> param = new HashMap<>();
	private Class<BoardMapper> cls = BoardMapper.class;
	public int count(String boardid) {
		return template.getMapper(cls).count(boardid);
	}
	public List<Board> list
	       (Integer pageNum,int limit, String boardid) {
		param.clear();
		int startrow = (pageNum - 1) * limit + 1; 
		int endrow = startrow + limit - 1; //startrow에서 limit만큼
		param.put("startrow", startrow);
		param.put("endrow", endrow);
		param.put("boardid", boardid);
		return template.getMapper(cls).list(param);
	}
	//게시판 번호의 최대값
	private int maxNum() {
		return template.getMapper(cls).maxNum();
	}
	public void insert(Board board) {
		int num = maxNum() + 1;
		board.setNum(num); 
		board.setGrp(num);  
		template.getMapper(cls).insert(board); 
	}
	public Board selectOne(Integer num) {
		return template.getMapper(cls).selectOne(num);
	}
	public void readcntadd(Integer num) {
		template.getMapper(cls).readcntadd(num);		
	}
}