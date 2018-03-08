package com.chatbot.apiBanco;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@SpringBootApplication
public class ApiBancoApplication extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.antMatcher("/**")
				.authorizeRequests()
				.antMatchers("/**","/test**", "/cliente/crea**")
				.permitAll()
				.anyRequest()
				.authenticated();

		http.csrf().disable();
	}

	public static void main(String[] args) {
		SpringApplication.run(ApiBancoApplication.class, args);
	}
}
