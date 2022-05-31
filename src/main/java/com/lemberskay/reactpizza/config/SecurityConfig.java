package com.lemberskay.reactpizza.config;

import com.lemberskay.reactpizza.model.entity.User;
import com.lemberskay.reactpizza.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableConfigurationProperties
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    UserServiceImpl userService;
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/users/**").hasAuthority(User.Role.ROLE_ADMIN.toString())
                .antMatchers(POST, "/api/categories/**","/api/menu/**","/api/countries/**").hasAuthority(User.Role.ROLE_ADMIN.toString())
                .antMatchers(PUT, "/api/categories/**","/api/menu/**","/api/countries/**").hasAuthority(User.Role.ROLE_ADMIN.toString())
                .antMatchers(DELETE, "/api/categories/**","/api/menu/**","/api/countries/**").hasAuthority(User.Role.ROLE_ADMIN.toString())
                .antMatchers(GET,"/api/orders", "api/orders/{id}").hasAuthority(User.Role.ROLE_ADMIN.toString())
                .antMatchers(GET, "/api/addresses").hasAuthority(User.Role.ROLE_ADMIN.toString())
                .antMatchers("/api/addresses/**").permitAll()
                .antMatchers("/api/orders/**").permitAll()
                .antMatchers(POST,"/api/users/**").permitAll()
                .antMatchers(GET, "/api/categories/**","/api/menu/**","/api/countries/**").permitAll()
                .antMatchers("/api/addresses/**").permitAll()
                .anyRequest().authenticated()
                .and().httpBasic()
                .and().sessionManagement().disable();
    }

@Autowired
protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder());
}

}
