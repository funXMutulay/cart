package org.quantum.nine.jcart.admin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, proxyTargetClass = true)

public class WebSecurityConfig  extends WebSecurityConfigurerAdapter{
	
	
	
		
		@Autowired
		private UserDetailsService customUserDetailsService;
		
		@Bean
	    public BCryptPasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
		
		@Autowired
	    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	        auth
	        	.userDetailsService(customUserDetailsService)
	        	.passwordEncoder(passwordEncoder());
	    }
		
	    @Override
	    protected void configure(HttpSecurity http) throws Exception {
	        http
	        	.csrf().disable()
	            .authorizeRequests()
	            	.antMatchers("/resources/**", "/webjars/**","/assets/**").permitAll()
	                .antMatchers("/", "/forgotPwd","/resetPwd").permitAll()
	                .anyRequest().authenticated()
	                .and()
	            .formLogin()
	                .loginPage("/login")
	                .defaultSuccessUrl("/home")
	                .failureUrl("/login?error")
	                .permitAll()
	                .and()
	            .logout()
	            	.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
	            	.permitAll()
	                .and()
	            .exceptionHandling().accessDeniedPage("/403");
	    }    
	

}
