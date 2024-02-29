package com.ll.security1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

	@GetMapping({"", "/"})
	public String index() {
		return "index";
	}

	@GetMapping("/user")
	public String user() {
		return "user";
	}

	@GetMapping("/admin")
	@ResponseBody
	public String admin() {
		return "admin";
	}

	@GetMapping("/manager")
	@ResponseBody
	public String manager() {
		return "manager";
	}

	//원래는 시큐리티가 낚아챘지만, SecurityConfig파일 생성 후 작동안함
	@GetMapping("/login")
	@ResponseBody
	public String login() {
		return "login";
	}

	@GetMapping("/join")
	@ResponseBody
	public String join() {
		return "join";
	}


	@GetMapping("/joinProc")
	@ResponseBody
	public String joinProc() {
		return "회원가입 완료됨";
	}

}
