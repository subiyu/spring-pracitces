package com.poscodx.guestbook.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.poscodx.guestbook.repository.GuestBookLogRepository;
import com.poscodx.guestbook.repository.GuestBookRepository;
import com.poscodx.guestbook.vo.GuestBookVo;

public class GuestBookService {
	@Autowired
	private GuestBookRepository guestBookRepository;
	
	@Autowired
	private GuestBookLogRepository guestBookLogRepository;
	
	public List<GuestBookVo> getContentsList() {
		return guestBookRepository.findAll();
	}
	
	public void deleteContents(Long no, String password) {
		guestBookLogRepository.update(no);
		guestBookRepository.deleteByNoAndPassword(no, password);
	}
	
	public void addContents(GuestBookVo vo) {
		TransactionSynchronizationManager.initSynchronization();
		int count = guestBookLogRepository.update();
		
		if(count == 0) {
			guestBookLogRepository.insert();
		}
		
		guestBookRepository.insert(vo);
	}
}
