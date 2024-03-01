package com.ll.security1.auth;

//시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킨다.
//로그인을 진행이 완료가 되면 시큐리티가 session을 만들어준다. (Security ContextHolder)
// Security ContextHolder에 들어갈수 있는 오브젝트는 정해져 있음
// 오브젝트 타입 => Authentication 타입의 객체
// Authentication 안에 User정보가 있어야 됨
//User오브젝트의 타입 => UserDetail 타입 객체

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ll.security1.member.entity.Member;

/** 정리
 * SecuritySession(Security ContextHolder) 안에는 Authentication 타입의 객체만 들어갈 수 있다.
 * Authentication 객체에 User정보를 저장하는데, User정보 객체 => UserDetails 타입이어야한다.
 * 현제 UserDetail -> UserDetails타입
 */

public class UserDetail implements UserDetails {

	private Member member; // 콤포지션 (콤포지션 : 한 클래스가 다른 클래스의 인스턴스를 포함하는 방식)

	public UserDetail(Member member) {
		this.member = member;
	}

	//해당 User의 권한을 리턴하는 곳
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collect = new ArrayList<>();
		collect.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {
				return member.getRole();
			}
		});
		return collect;
	}

	@Override
	public String getPassword() {
		return member.getPassword();
	}

	@Override
	public String getUsername() {
		return member.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {

		//만약에 1년동안 회원이 로그인을 안하면 -> 휴면계정으로 하기로 함
		//member.getLoginDate() => 현제시간 - 로그인시간 >= 1년을 초과하면 false로 리턴

		return true;
	}
}
