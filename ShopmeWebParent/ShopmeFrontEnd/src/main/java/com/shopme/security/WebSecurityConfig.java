package com.shopme.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.shopme.security.oauth.CustomerOauth2UserService;
import com.shopme.security.oauth.OAuth2LoginSuccessHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomerOauth2UserService customerOauth2UserService;
	@Autowired
	private OAuth2LoginSuccessHandler auth2LoginSuccessHandler;
	@Autowired
	private DatabaseLoginSuccessHandler databaseLoginSuccessHandler;
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/account_details","/update_account_details","/cart","/orders/**",
					"/address_book/**","/checkout","/place_order","/process_paypal_order").authenticated()
			.anyRequest().permitAll()
			.and()
			.formLogin()
				.loginPage("/login")
				.usernameParameter("email")
				.successHandler(databaseLoginSuccessHandler)
				.permitAll()
			.and()
			.oauth2Login()
				.loginPage("/login")
				.userInfoEndpoint()
				.userService(customerOauth2UserService)
				.and()
				.successHandler(auth2LoginSuccessHandler)
			.and()
			.logout().permitAll()
			.and()
			.rememberMe()
				.key("1234567890_aBcDeFgHiJkLmNoPqRsTuVxYzW")
				.tokenValiditySeconds(14*24*60*60)
			.and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
		
			
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**");
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return new CustomerUserDetailsService();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider =  new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		
		return authProvider;
	}
	
}
