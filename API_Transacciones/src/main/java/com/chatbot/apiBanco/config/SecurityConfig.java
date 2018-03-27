package com.chatbot.apiBanco.config;


import com.chatbot.apiBanco.model.database.repository.AuthuserRepository;
import com.chatbot.apiBanco.model.database.tables.Authuser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends  WebSecurityConfigurerAdapter{

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    AuthuserRepository authuserRepo;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/cliente", "/cuenta", "/tarjeta", "/transaccion").hasRole("db_admin")
                .antMatchers("/v1/**").hasRole("chatbot")
                .antMatchers("/credentials/**", "/authusers").hasRole("apimaster")
                .and()
                .httpBasic()
                .and()
                .formLogin();

        http.csrf().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {

        authuserRepo.findAll().forEach(
                (e) -> {
                    try {
                        auth.inMemoryAuthentication()
                                .passwordEncoder(passwordEncoder())
                                .withUser(e.getUsername())
                                .password(e.getPassword())
                                .roles(e.getRol());

                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
        );

    }

}