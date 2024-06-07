package com.poscodx.guestbook.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.poscodx.guestbook.repository.template.JdbcContext;
import com.poscodx.guestbook.vo.GuestBookVo;

//공부용
@Repository
public class GuestBookRepositoryWithJdbcContext {
	private JdbcContext jdbcContext;

	public GuestBookRepositoryWithJdbcContext(JdbcContext jdbcContext) {
		this.jdbcContext = jdbcContext;
	}
	
	public List<GuestBookVo> findAll() {
		return jdbcContext.query( 		//select문은 executeQuery -> JdbcTemplate.query()
				"SELECT no, name, password, contents, date_format(reg_date, '%Y/%m/%d %H:%i:%s') FROM guestbook ORDER BY reg_date desc", 
				new RowMapper<GuestBookVo>() { 	//객체에 매핑
					@Override
					public GuestBookVo mapRow(ResultSet rs, int rowNum) throws SQLException {
						GuestBookVo vo = new GuestBookVo();
						vo.setNo(rs.getLong(1));
						vo.setName(rs.getString(2));
						vo.setPassword(rs.getString(3));
						vo.setContents(rs.getString(4));
						vo.setRegDate(rs.getString(5));
						
						return vo;
					}
			
		});
	}

	public int deleteByNoAndPassword(Long no, String password) {
		return jdbcContext.executeUpdate("DELETE FROM guestbook WHERE no = ? AND password = ?", 
				new Object[] {no, password});
	}
	
	public int insert(GuestBookVo vo) {
		return jdbcContext.executeUpdate("INSERT INTO guestbook(name, password, contents, reg_date) VALUES(?, ?, ?, now())", 
				new Object[] {vo.getName(), vo.getPassword(), vo.getContents()});
	}

}
