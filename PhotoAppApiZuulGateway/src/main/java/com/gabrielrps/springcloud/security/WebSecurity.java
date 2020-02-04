package com.gabrielrps.springcloud.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter{

	private final Environment enviroment;
	
	public WebSecurity(Environment enviroment) {
		this.enviroment = enviroment;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.csrf().disable();
		http.headers().frameOptions().disable();
		
		http.authorizeRequests()
		.antMatchers(enviroment.getProperty("api.users.actuator.url.path")).permitAll()
		.antMatchers(enviroment.getProperty("api.zuul.actuator.url.path")).permitAll()
		.antMatchers(HttpMethod.POST, enviroment.getProperty("api.registration.url.path")).permitAll()
		.antMatchers(HttpMethod.POST, enviroment.getProperty("api.login.url.path")).permitAll()
		.anyRequest().authenticated()
		.and()
		.addFilter(new AuthorizationFilter(authenticationManager(), enviroment));
		
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
	}
	
}
