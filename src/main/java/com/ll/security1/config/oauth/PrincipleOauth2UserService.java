package com.ll.security1.config.oauth;

import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.ll.security1.auth.CustomUserDetail;
import com.ll.security1.config.oauth.provider.FaceBookUserInfo;
import com.ll.security1.config.oauth.provider.GoogleUserInfo;
import com.ll.security1.config.oauth.provider.NaverUserInfo;
import com.ll.security1.config.oauth.provider.OAuth2UserInfo;
import com.ll.security1.member.entity.Member;
import com.ll.security1.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrincipleOauth2UserService extends DefaultOAuth2UserService {

	private final MemberRepository memberRepository;


	//구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수

	// 함수 종료시 @AuthenticationPrincippal 어노테이션이 만들어 진다.
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		// 서버의 기본 정보 registrationId로 어떤 OAuth로 로그인 했는지 확인 가능
		System.out.println("userRequest.getClientRegistration() = " + userRequest.getClientRegistration());
		System.out.println("userRequest.getAccessToken() = " + userRequest.getAccessToken().getTokenValue());



		OAuth2User oAuth2User = super.loadUser(userRequest);
		// 구글 로그인 버튼 클릭 -> 구글 로그인 창 -> 로그인 완료 -> code를 리턴(OAuth2-Client라이브러리) -> Access토큰 요청
		// userRequest 정보 -> loadUser함수 호출 -> 구글로 부터 회원 프로필을 받아줌
		System.out.println("getAttributes = " + oAuth2User.getAttributes());

		//회원가입 강제로 진행해볼 예정
		OAuth2UserInfo oAuth2UserInfo = null;
		if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
			System.out.println("구글 로그인 요청");
			oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
		} else if (userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
			System.out.println("페이스북 로그인 요청");
			oAuth2UserInfo = new FaceBookUserInfo(oAuth2User.getAttributes());
		} else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
			System.out.println("네이버 로그인 요청");
			//getAttributes = {resultcode=00, message=success, response={id=???, email=???@???.com, name=???}} 이런식의 형태로 옴 그래서 밑으로 변경해야함
			oAuth2UserInfo = new NaverUserInfo((Map)oAuth2User.getAttributes().get("response"));
		} else {
			System.out.println("우리는 구글과 페이스북과 네이버만 지원해요 ㅎㅎㅎ");
		}

		String provider = oAuth2UserInfo.getProvider();
		String providerId = oAuth2UserInfo.getProviderId();
		String username = provider + "_" + providerId; // google_402104210010204
		String password = new BCryptPasswordEncoder().encode("겟인데어"); //OAuth2 로그인은 username, password가 큰 의미가 없다.
		String email = oAuth2UserInfo.getEmail();
		String role = "ROLE_USER";

		Member member = memberRepository.findByUsername(username);

		if (member == null) {
			System.out.println("OAuth 로그인이 최초입니다.");
			member = Member.builder()
				.username(username)
				.password(password)
				.email(email)
				.role(role)
				.provider(provider)
				.providerId(providerId)
				.build();
			memberRepository.save(member);
		}else{
			System.out.println("로그인을 이미 한 적이 있습니다. 당신은 자동회원가입이 되어 있습니다.");
		}
		//TODO 프로젝트에서는 추가 정보 입력하고 회원가입 진행


		//Authentication 객체 안으로 들어감
		return new CustomUserDetail(member, oAuth2User.getAttributes());
	}
}
