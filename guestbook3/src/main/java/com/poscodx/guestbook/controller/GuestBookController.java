package com.poscodx.guestbook.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.poscodx.guestbook.repository.GuestBookRepositoryWithJdbcContext;
import com.poscodx.guestbook.repository.GuestBookRepositoryWithJdbcTemplate;
import com.poscodx.guestbook.repository.GuestBookRepositoryWithRawJdbc;
import com.poscodx.guestbook.vo.GuestBookVo;

@Controller
public class GuestBookController {
	@Autowired
	private GuestBookRepositoryWithJdbcContext guestbookRepository2;
	
	@Autowired
	private GuestBookRepositoryWithJdbcTemplate guestbookRepository3;
	
	@RequestMapping("/")
	public String index(Model model) {
		List<GuestBookVo> list = guestbookRepository2.findAll();
		model.addAttribute("list", list);
		
		return "index";
	}
	
	@RequestMapping(value="/add", method=RequestMethod.POST)
	public String add(GuestBookVo vo) {
		guestbookRepository2.insert(vo);
		
		return "redirect:/";
	}
	
	@RequestMapping(value="/delete", method=RequestMethod.GET)
	public String delete(Long no, Model model) {
		model.addAttribute("no", no);
		
		return "delete";
	}
	
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public String delete(GuestBookVo vo) {
		guestbookRepository2.deleteByNoAndPassword(vo.getNo(), vo.getPassword());
		
		return "redirect:/";
	}
}