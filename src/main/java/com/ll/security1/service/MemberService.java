package com.ll.security1.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ll.security1.member.entity.Member;
import com.ll.security1.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;


	public Member join(Member member) {
		String encodePw = bCryptPasswordEncoder.encode(member.getPassword());
		member.setPassword(encodePw);
		member.setRole("ROLE_USER");
		memberRepository.save(member);
		return member;
	}

}
