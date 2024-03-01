package com.ll.security1.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ll.security1.auth.CustomUserDetail;
import com.ll.security1.member.entity.Member;
import com.ll.security1.service.MemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class IndexController {
	private final MemberService memberService;

	@GetMapping("/test/login")
	@ResponseBody
	public String loginTest(Authentication authentication,
		@AuthenticationPrincipal CustomUserDetail userDetails) { //DI(의존성 주입)
		//유저 정보 받는 법
		System.out.println("/test/login ==============================");
		System.out.println("authentication.getPrincipal() = " + authentication.getPrincipal());
		System.out.println("userDetails.getUsername() = " + userDetails.getUsername());
		System.out.println("userDetails.getMember() = " + userDetails.getMember());

		return "세션 정보 확인하기";
	}

	@GetMapping({"", "/"})
	public String index() {
		return "index";
	}

	@GetMapping("/user")
	@ResponseBody
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
		return "redirect:/login";
	}


	@GetMapping("/joinProc")
	@ResponseBody
	public String joinProc() {
		return "회원가입 완료됨";
	}

	@Secured("ROLE_ADMIN") // 간단하게 권한 검사 하고 싶을 때는 Secured 어노테이션 사용
	@GetMapping("/info")
	@ResponseBody
	public String info() {
		return "개인정보";
	}


	@PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") // 액션메서드 실행전에 실행됨, 여러개의 권한을 걸고 싶을 시
	@GetMapping("/data")
	@ResponseBody
	public String data() {
		return "데이터 정보";
	}

}
