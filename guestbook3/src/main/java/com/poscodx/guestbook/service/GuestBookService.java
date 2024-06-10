package com.poscodx.guestbook.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.poscodx.guestbook.repository.GuestBookLogRepository;
import com.poscodx.guestbook.repository.GuestBookRepository;
import com.poscodx.guestbook.vo.GuestBookVo;

@Service
public class GuestBookService {
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private PlatformTransactionManager transactionManager;
	
	@Autowired
	private GuestBookRepository guestBookRepository;
	
	@Autowired
	private GuestBookLogRepository guestBookLogRepository;
	
	public List<GuestBookVo> getContentsList() {
		return guestBookRepository.findAll();
	}
	
	public void deleteContents(Long no, String password) {
		// TX:BEGIN ////////////////////
		TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

		try {
			guestBookLogRepository.update(no);
			guestBookRepository.deleteByNoAndPassword(no, password);
			
			// TX:END(SUCCESS) ///////////
			transactionManager.commit(status);
		} catch(Throwable e) {
			//TX:END(FAIL) /////////////////
			transactionManager.rollback(status);
		}
	}
	
	public void addContents(GuestBookVo vo) {
		// 트랜잭션 동기
		TransactionSynchronizationManager.initSynchronization();
		
		Connection conn = DataSourceUtils.getConnection(dataSource);
		try {
			// TX:BEGIN ///////////////////
			conn.setAutoCommit(false);
			int count = guestBookLogRepository.update();
			
			if(count == 0) {
				guestBookLogRepository.insert();
			}

			guestBookRepository.insert(vo);
			
			// TX:END(SUCCESS) /////////////
			conn.commit();
		} catch(Throwable e) {
			// TX:END(FAIL) /////////////////////
			try {
				conn.rollback();				
			} catch(SQLException ignored) {
			}
		} finally {
			DataSourceUtils.releaseConnection(conn, dataSource);
		}
		
	
	}
}
