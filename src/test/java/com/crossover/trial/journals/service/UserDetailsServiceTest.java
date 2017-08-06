package com.crossover.trial.journals.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Optional;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.crossover.trial.journals.Application;
import com.crossover.trial.journals.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class UserDetailsServiceTest {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private UserService userService;

	private String existingUserName = "kibarayhan@gmail.com";

	@Test
	public void loadUserByUsername() {
		UserDetails currentUser = userDetailsService.loadUserByUsername(existingUserName);
		assertNotNull(currentUser);

		Optional<User> user = userService.getUserByLoginName(existingUserName);
		assertNotNull(user.get());

		assertEquals(user.get().getLoginName(), currentUser.getUsername());
		assertEquals(user.get().getPwd(), currentUser.getPassword());
	}

	@Test
	public void loadUserByUsername_GivenNonExistUser() {
		assertThatThrownBy(() -> userDetailsService.loadUserByUsername("Not_existing_UserName"))
				.isInstanceOf(UsernameNotFoundException.class);
	}
}
