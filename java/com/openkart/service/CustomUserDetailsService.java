package com.openkart.service;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.openkart.model.Buyer;
import com.openkart.repository.BuyerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;

@Service
public class CustomUserDetailsService implements UserDetailsService
{
	
	@Autowired
	private BuyerRepository buyerRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException 
	{
		
		if(username.equals("abhi"))
		{
			return new User("abhi","454566",new ArrayList<>());
		}
		
		else if(!username.equals("abhi"))
			 {
			 
			  Optional<Buyer> buyer=  buyerRepo.findById(Integer.parseInt(username));
			  String password=buyer.get().getBuyerPassword();
			 
			  return new User(username,password,new ArrayList<>());
			     
			 }
		
		else
		{
			throw new UsernameNotFoundException("Username Not Found");
		}
		
		
		
		
	}
	

}
