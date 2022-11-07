package dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;

import logic.Board;

public interface BoardMapper {
	
	@Select("select count(*) from board where boardid=#{value}")
	int count(String boardid);
	
	String sql = "select * from"
			+ "(select rownum rnum, num, writer, subject, content,"
			+ " file1 fileurl, regdate, grp, grplevel, grpstep, pass, readcnt from"
			+ " (select * from board where boardid=#{boardid}"
			+ " order by grp desc, grpstep asc))"
			+ " where rnum between #{startrow} and #{endrow}";
	@Select(sql)
	List<Board> list(Map<String , Object> param);
}
