package com.poscodx.guestbook.repository;

import org.springframework.stereotype.Repository;

import com.poscodx.guestbook.repository.template.JdbcContext;
import com.poscodx.guestbook.vo.GuestBookVo;

@Repository
public class GuestBookRepositoryWithJdbcContext {
	private JdbcContext jdbcContext;

	public GuestBookRepositoryWithJdbcContext(JdbcContext jdbcContext) {
		this.jdbcContext = jdbcContext;
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
