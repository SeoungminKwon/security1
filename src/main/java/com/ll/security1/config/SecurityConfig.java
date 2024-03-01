package com.ll.security1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.ll.security1.config.oauth.PrincipleOauth2UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록이 됨
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured 어노테이션 활성화, preAuthorize, postAuthorize 어노테이션 활성화
public class SecurityConfig{

	private final PrincipleOauth2UserService principleOauth2UserService;

	@Bean //@Bean - 해당 메서드의 리턴되는 오브젝트를 IoC로 등록해줌
	public BCryptPasswordEncoder encodePwd() {
		return new BCryptPasswordEncoder();
	}


	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf ->
				csrf.disable()
			)
			.authorizeHttpRequests(request ->
				request.requestMatchers("/user/**").authenticated()
					.requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER")
					.requestMatchers("/admin/**").hasRole("ADMIN")
					.anyRequest().permitAll()
			)
			.formLogin(formLogin ->
				formLogin.loginPage("/login")
					.loginProcessingUrl("/login") //login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행한다.
					.defaultSuccessUrl("/") // loginPage에서 로그인하면 홈으로 보내주는데, 특정페이지로 가려다가 로그인하려고 하면 특정페이지로 보내줌
			)
			.oauth2Login(
				oauth2 ->
					oauth2.loginPage("/login") // 구글 로그인이 완료된 뒤의 후처리가 필요함
						.userInfoEndpoint(userinfo ->
							userinfo.userService(principleOauth2UserService)
						)
						
				/** 과정
				 *  1. 코드 받기(인증)
				 *  2. 액세스 토큰(권한)
				 *  3. 사용자 프로필 정보 가져오기
				 *  4-1. 그 정보를 토대로 회원가입을 자동으로 진행시키기도 함
				 *  4-2. 제공받은 정보가 부족하면 , ex.쇼핑몰 -> 주소, 등급 등... 의 추가적인 정보가 필요 -> 추가적인 회원가입 필요
				 *  Tip. 구글 로그인 완료시 -> 코드X, (엑세스 토큰, 프로필 정보)O 를 받아옴 -> OAuth2 client 편리함
				 */
			);
		
		return http.build();
	}

}

