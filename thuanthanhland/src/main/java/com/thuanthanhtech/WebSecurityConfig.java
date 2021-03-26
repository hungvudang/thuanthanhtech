package com.thuanthanhtech;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

	@Bean
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public static HttpFirewall defaultHttpFirewall() {
		return new DefaultHttpFirewall();
	}

	@Configuration
	@Order(1)
	public static class AdminWebSecurityConfig extends WebSecurityConfigurerAdapter {

		@Autowired
		private UserDetailsService userDetailsService;

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
		}

		@Override
		public void configure(WebSecurity web) throws Exception {
			web.httpFirewall(defaultHttpFirewall());
		}
		
		
		
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			//@formatter:off
			http
				.antMatcher("/admin/**").authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN")
				.and()
					.formLogin()
					.loginProcessingUrl("/admin/j_spring_security_login")
					.loginPage("/login")
					.usernameParameter("username")
					.passwordParameter("password")
					.defaultSuccessUrl("/admin/home")
					.failureUrl("/login?error")
					.and()
						.logout()
						.logoutUrl("/admin/j_spring_security_logout")
						.clearAuthentication(true)
						.invalidateHttpSession(true)
						.deleteCookies("JSESSIONID")
						.logoutSuccessUrl("/login?logout")
						.and()
							.exceptionHandling()
							.accessDeniedPage("/errors/403")
							.and()
								.httpBasic();
			
			//@formatter:on

		}
	}

	@Configuration
	@Order(2)
	public static class UserWebSecurityConfig extends WebSecurityConfigurerAdapter {

		@Autowired
		private UserDetailsService userDetailsService;

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
		}

		@Override
		public void configure(WebSecurity web) throws Exception {
			super.configure(web);
			web.httpFirewall(defaultHttpFirewall());
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			//@formatter:off
			http
				.antMatcher("/du-an/**").authorizeRequests().anyRequest().hasAnyRole("ADMIN", "USER")
				.and()
					.formLogin()
					.loginProcessingUrl("/public/j_spring_security_login")
					.loginPage("/dang-nhap")
					.usernameParameter("username")
					.passwordParameter("password")
					.defaultSuccessUrl("/")
					.failureUrl("/dang-nhap?error")
						
				.and()
					.logout()
					.logoutUrl("/public/j_spring_security_logout")
					.clearAuthentication(true)
					.invalidateHttpSession(true)
					.deleteCookies("JSESSIONID")
					.logoutSuccessUrl("/dang-nhap?logout")
						
				.and()
					.httpBasic();
			
			//@formatter:on

		}
	}
	
	@Configuration
	@Order(3)
	public static class NormalWebSecurityConfig extends WebSecurityConfigurerAdapter {

		@Autowired
		private UserDetailsService userDetailsService;

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
		}

		@Override
		public void configure(WebSecurity web) throws Exception {
			super.configure(web);
			web.httpFirewall(defaultHttpFirewall());
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			//@formatter:off
			http
				.authorizeRequests().anyRequest().permitAll()
				.and()
					.httpBasic();
			
			//@formatter:on

		}
	}
	
}
