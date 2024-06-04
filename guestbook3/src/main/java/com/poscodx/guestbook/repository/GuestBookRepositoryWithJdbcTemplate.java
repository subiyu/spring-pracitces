package com.poscodx.guestbook.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.poscodx.guestbook.vo.GuestBookVo;

@Repository
public class GuestBookRepositoryWithJdbcTemplate {
	private JdbcTemplate jdbcTemplate;

	public GuestBookRepositoryWithJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<GuestBookVo> findAll() {
		return jdbcTemplate.query( 		//select문은 executeQuery -> JdbcTemplate.query()
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
	
	public int insert(GuestBookVo vo) {
		return jdbcTemplate.update(		//select이외의 DML문은 executeUpdate -> JdbcTemplate.update()
				"INSERT INTO guestbook(name, password, contents, reg_date) VALUES(?, ?, ?, now())", 
				new Object[] {vo.getName(), vo.getPassword(), vo.getContents()});
		
		// 기변 파라미터 지원 해줌
//		return jdbcTemplate.update(
//				"INSERT INTO guestbook(name, password, contents, reg_date) VALUES(?, ?, ?, now())", 
//				vo.getName(), vo.getPassword(), vo.getContents());
	}

	public int deleteByNoAndPassword(Long no, String password) {
		return jdbcTemplate.update("DELETE FROM guestbook WHERE no = ? AND password = ?", 
				new Object[] {no, password});
	}
}
