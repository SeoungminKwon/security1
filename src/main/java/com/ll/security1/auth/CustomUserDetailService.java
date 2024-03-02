package com.ll.security1.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ll.security1.member.entity.Member;
import com.ll.security1.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

// 시큐리티 설정에서 loginProcessingUrl("/login");
// login 요청이 오면 자동으로 UserDetailsService 타입으로 IOC 되어있는 loadUserByUsername 함수가 실행됨 (규칙)
// 규칙이기 때문에 UserDetailsService를 구현한 클래스에서 loadUserByUsername를 작성해줘야한다.
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

	private final MemberRepository memberRepository;


	// 시큐리티 session = Authentication = UserDetails
	// loadUserByUsername에서 리턴된 값이 Authentication(내부 UserDetails) 이렇게 들어감
	// 마지막에는  시큐리티 session(내부 Authentication(내부 UserDetails))이렇게 들어감

	// 함수 종료시 @AuthenticationPrincippal 어노테이션이 만들어 진다.
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//loginForm 에서 name="username2"라고 적으면 안받아짐 -> 즉 loginForm에서 name가 맞춰줘야한다. (만약 바꿀꺼면 security config에서 usernameParameter를 설정해줘야함)

		Member findMember = memberRepository.findByUsername(username);
		if (findMember != null) {
			return new CustomUserDetail(findMember);
		}
		return null;
	}
}
