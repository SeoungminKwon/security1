package com.ll.security1.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
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

	@GetMapping("/test/oauth/login")
	@ResponseBody
	public String testOAuthLogin(
		Authentication authentication, //DI(의존성 주입)
		@AuthenticationPrincipal OAuth2User oauth
	) {
		System.out.println("/test/oauth/login ===========================");
		OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
		//밑의 둘의 정보가 같은 것을 알 수 있음
		System.out.println("oAuth2User.getAttributes() = " + oAuth2User.getAttributes());
		System.out.println("oauth.getAttributes() = " + oauth.getAttributes());

		return "OAuth2 세션 정보 확인하기";
	}



	@GetMapping({"", "/"})
	public String index() {
		return "index";
	}

	//OAuth2 로그인을 해도 UserDetails
	//일반 로그인을 해도 UserDetails 로 받을 수 있음
	// @AuthenticationPrincipal로 접근해도 다운 캐스팅 할 필요가 없음
	// UserService, PrincipalOAuth2UserService는 만들지않아도 loadUser가 발동을 함 -> CustomUserDetail을 리턴하기 위해서 만듬
	// 리턴되는 CustomUserDetails 가 Authentication에 저장된다.
	// UserService, PrincipalOAuth2UserService를 오버라이딩 한이유는 1. CustomUserDetails를 리턴하기 위함 // 2. OAuth로 로그인시 회원가입을 강제로 진행시키기 위함
	@GetMapping("/user")
	@ResponseBody
	public String user(
		@AuthenticationPrincipal CustomUserDetail userDetail
	) {
		System.out.println("userDetail.getMember() = " + userDetail.getMember());
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
