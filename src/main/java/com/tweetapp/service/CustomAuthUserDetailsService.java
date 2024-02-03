package com.tweetapp.service;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tweetapp.repository.UserDetailsRepository;

@Service
public class CustomAuthUserDetailsService implements UserDetailsService{
	
	@Autowired
	private UserDetailsRepository userDetailsRepository;

	@Override
	public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
		com.tweetapp.model.UserDetails user = userDetailsRepository.findByLoginId(loginId);
		return new User(user.getLoginId(),user.getPassword(),new ArrayList<>());
	}

	
}
