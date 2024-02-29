package com.ll.security1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록이 됨
public class SecurityConfig{

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
					.defaultSuccessUrl("/")
			);
		
		return http.build();
	}

}

