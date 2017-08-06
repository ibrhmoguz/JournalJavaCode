package com.crossover.trial.journals.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserDetailsService implements UserDetailsService {

	private final UserService userService;

	@Autowired
	public CurrentUserDetailsService(UserService userService) {
		this.userService = userService;
	}

	@Override
	public UserDetails loadUserByUsername(String email){
		com.crossover.trial.journals.model.User user = userService.getUserByLoginName(email).orElseThrow(
				() -> new UsernameNotFoundException(String.format("User with email=%s was not found", email)));

		return new User(user.getLoginName(), user.getPwd(), true, true, true, true,
				AuthorityUtils.createAuthorityList(user.getRole().toString()));
	}
	
}
