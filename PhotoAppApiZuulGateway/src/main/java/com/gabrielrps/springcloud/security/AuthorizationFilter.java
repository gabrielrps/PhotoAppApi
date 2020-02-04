package com.gabrielrps.springcloud.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.gabrielrps.springcloud.exception.TokenExpired;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

public class AuthorizationFilter extends BasicAuthenticationFilter{

	private final Environment enviroment;

	public AuthorizationFilter(AuthenticationManager authenticationManager, Environment enviroment) {
		super(authenticationManager);
		this.enviroment = enviroment;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
	
		String autorizationHeader = request.getHeader(enviroment.getProperty("authorization.token.header.name"));
		
		if(autorizationHeader == null || !autorizationHeader.startsWith(enviroment.getProperty("authorization.token.header.prefix"))) {
			chain.doFilter(request, response);
			return;
		}
		
		UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(request, response);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		String autorizationHeader = request.getHeader(enviroment.getProperty("authorization.token.header.name"));
		
		if(autorizationHeader == null) return null;
		
		String token = autorizationHeader.replace(enviroment.getProperty("authorization.token.header.prefix"), "");
		
		Jws<Claims> parseClaimsJws = null;
		
		try {
			parseClaimsJws = Jwts.parser().setSigningKey(enviroment.getProperty("token.secret")).parseClaimsJws(token);
		} catch (ExpiredJwtException e) {
			throw new TokenExpired("Token was expired " + e.getMessage());
		}
		
		
		String userId = parseClaimsJws
						.getBody()
						.getSubject();
		
		if(userId == null) return null;
		
		return new UsernamePasswordAuthenticationToken(userId, null, new ArrayList<>());
		
	}

}
