package com.ll.security1.config.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipleOauth2UserService extends DefaultOAuth2UserService {

	//구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		// 서버의 기본 정보 registrationId로 어떤 OAuth로 로그인 했는지 확인 가능
		System.out.println("userRequest.getClientRegistration() = " + userRequest.getClientRegistration());
		System.out.println("userRequest.getAccessToken() = " + userRequest.getAccessToken().getTokenValue());

		// 구글 로그인 버튼 클릭 -> 구글 로그인 창 -> 로그인 완료 -> code를 리턴(OAuth2-Client라이브러리) -> Access토큰 요청
		// userRequest 정보 -> loadUser함수 호출 -> 구글로 부터 회원 프로필을 받아줌
		System.out.println(
			"super.loadUser(userRequest).getAttributes() = " + super.loadUser(userRequest).getAttributes());


		//회원가입 강제로 진행해볼 예정

		return super.loadUser(userRequest);
	}
}
