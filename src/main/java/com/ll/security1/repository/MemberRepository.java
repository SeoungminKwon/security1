package com.ll.security1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.security1.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
