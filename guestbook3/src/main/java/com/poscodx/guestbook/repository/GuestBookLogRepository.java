package com.poscodx.guestbook.repository;

import org.springframework.stereotype.Repository;

import com.poscodx.guestbook.repository.template.JdbcContext;

@Repository
public class GuestBookLogRepository {
	private JdbcContext jdbcContext;

	public GuestBookLogRepository(JdbcContext jdbcContext) {
		this.jdbcContext = jdbcContext;
	}
	
	public int insert() {
		return jdbcContext.update("INSERT INTO guestbook_log VALUES(current_date(), 1)");
	}
	
	public int update() {
		return jdbcContext.update("UPDATE guestbook_log SET count = count + 1 WHERE date = current_date()");
	}

	public int update(Long no) {
		return jdbcContext.update("UPDATE guestbook_log SET count = count - 1 WHERE date = (SELECT date(reg_date) FROM guestbook WHERE no = ?)", new Object[] {no});
	}
	
}
