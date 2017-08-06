package com.crossover.trial.journals.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.crossover.trial.journals.Application;
import com.crossover.trial.journals.model.Role;
import com.crossover.trial.journals.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserServiceTest {

	@Autowired
	private UserService userService;
	
	private User existingUser;
	private User notExistingUser;

	@Before
	public void setUp() throws Exception {
		User user = new User();
		user.setEnabled(true);
		user.setPwd("password");
		user.setLoginName("testUser@gmail.com");
		user.setRole(Role.USER);		
		existingUser = userService.save(user);
		
		notExistingUser = new User();
		notExistingUser.setEnabled(true);
		notExistingUser.setPwd("password");
		notExistingUser.setLoginName("notExistingUser@gmail.com");
		notExistingUser.setRole(Role.USER);		
	}
	
	@Test
	public void getAll_GivenExistingUser_ShouldExists(){
		Optional<User> user = userService.getUserByLoginName(existingUser.getLoginName());
		assertNotNull(user.get());

		List<User> existingUsers = userService.findAll();
		
		assertTrue(existingUsers.stream().anyMatch(u -> user.get().equals(u)));
		assertTrue(existingUsers.stream().anyMatch(u -> user.get().getEnabled().equals(u.getEnabled())));
	}
	
	@Test
	public void getAll_GivenNotExistingUser_ShouldNotExists(){
		List<User> existingUsers = userService.findAll();		
		assertFalse(existingUsers.stream().anyMatch(u -> notExistingUser.equals(u)));
	}	
}
