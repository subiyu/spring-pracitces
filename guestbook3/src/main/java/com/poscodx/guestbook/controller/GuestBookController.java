package com.poscodx.guestbook.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.poscodx.guestbook.repository.GuestBookRepository;
import com.poscodx.guestbook.service.GuestBookService;
import com.poscodx.guestbook.vo.GuestBookVo;

@Controller
public class GuestBookController {
	@Autowired
	private GuestBookService guestBookService;
	
	@RequestMapping("/")
	public String index(Model model) {
		List<GuestBookVo> list = guestBookService.getContentsList();
		model.addAttribute("list", list);
		
		return "index";
	}
	
	@RequestMapping(value="/add", method=RequestMethod.POST)
	public String add(GuestBookVo vo) {
		guestBookService.addContents(vo);
		
		return "redirect:/";
	}
	
	@RequestMapping(value="/delete/{no}", method=RequestMethod.GET)
	public String delete(@PathVariable("no") Long no, Model model) {
		model.addAttribute("no", no);
		
		return "delete";
	}
	
	@RequestMapping(value="/delete/{no}", method=RequestMethod.POST)
	public String delete(@PathVariable("no") Long no, @RequestParam(value="password", required=true, defaultValue="") String password) {
		//required=true: 필수 여부
		guestBookService.deleteContents(no, password);
		
		return "redirect:/";
	}
}