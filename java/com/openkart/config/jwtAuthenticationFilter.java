package com.openkart.config;

import java.io.IOException;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import com.openkart.helper.JwtUtil;
import com.openkart.service.CustomUserDetailsService;

import io.jsonwebtoken.ExpiredJwtException;



@Service
public class jwtAuthenticationFilter extends OncePerRequestFilter  {

	@Autowired
	private CustomUserDetailsService usersecurityimpl;
	
	@Autowired
	private JwtUtil jwtutil;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		final String requestTokenHeader=request.getHeader("Authorization");
		String username=null;
		String jwttoken=null;
		if(requestTokenHeader!=null && requestTokenHeader.startsWith("Bearer "))
		{
			jwttoken=requestTokenHeader.substring(7);
			try {
			username=jwtutil.extractUsername(jwttoken);
			}
			catch(ExpiredJwtException e) {
				e.printStackTrace();
				System.out.println("Jwt Token has Expired");
				
			}
			catch(Exception ex)
			{	ex.printStackTrace();
				System.out.println(ex);
			}
		}
		else
		{
			System.out.println("invalid Token not Start with Bearer");
		}
		
		
		if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null)
		{
			final UserDetails userDetails= this.usersecurityimpl.loadUserByUsername(username);
			
			if(this.jwtutil.validateToken(jwttoken, userDetails)) 
			{
				
				UsernamePasswordAuthenticationToken usernamePaswword=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
				 usernamePaswword.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePaswword);
			}
			
	
		
		}
		else
		{
			System.out.println("Token is not valid");
		}
		
	filterChain.doFilter(request, response);
	}

}
