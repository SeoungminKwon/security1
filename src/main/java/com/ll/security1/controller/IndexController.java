package com.ll.security1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ll.security1.member.entity.Member;
import com.ll.security1.service.MemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class IndexController {
	private final MemberService memberService;

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
	public String login() {
		return "loginForm";
	}

	@GetMapping("/joinForm")
	public String joinForm() {
		return "joinForm";
	}

	@PostMapping("/join")
	public String join(Member member) {
		memberService.join(member);
		return "redirect:/loginForm";
	}


	@GetMapping("/joinProc")
	@ResponseBody
	public String joinProc() {
		return "회원가입 완료됨";
	}

}
