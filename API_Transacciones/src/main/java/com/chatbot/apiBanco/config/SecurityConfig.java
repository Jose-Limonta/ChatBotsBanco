package com.chatbot.apiBanco.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends  WebSecurityConfigurerAdapter{

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/cliente", "/cuenta", "/tarjeta", "/transaccion").hasRole("db_admin")
                .antMatchers("/v1/**").hasRole("chatbot")
                .and()
                .httpBasic()
                .and()
                .formLogin();

        http.csrf().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        auth.inMemoryAuthentication()
            .passwordEncoder(NoOpPasswordEncoder.getInstance())
                .withUser("admin").password("password").roles("db_admin")
            .and()
                .withUser("chatbot").password("chatmin").roles("chatbot");

    }

}