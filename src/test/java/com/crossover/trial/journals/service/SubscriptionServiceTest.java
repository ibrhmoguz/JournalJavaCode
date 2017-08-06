package com.crossover.trial.journals.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
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
import com.crossover.trial.journals.dto.SubscriptionDTO;
import com.crossover.trial.journals.model.Category;
import com.crossover.trial.journals.model.Subscription;
import com.crossover.trial.journals.model.User;
import com.crossover.trial.journals.repository.CategoryRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SubscriptionServiceTest {

	@Autowired
	private SubscriptionService subscriptionService;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private UserService userService;

	private String existingUserName = "kibarayhan@gmail.com";

	private Category testCategory;
	private List<Category> categoryList;
	private SubscriptionDTO subscriptionDTO;
	
	@Before
	public void setUp() throws Exception {
		Category c = new Category();
		c.setName("Test Category");
		testCategory = categoryRepository.save(c);

		categoryList = new ArrayList<>();
		
		categoryList.add(testCategory);
		
		subscriptionDTO = new SubscriptionDTO(testCategory);
		subscriptionDTO.setActive(true);
	}
	
	@Test
	public void subscribe(){
		Optional<User> user = userService.getUserByLoginName(existingUserName);
		assertNotNull(user.get());
		
		List<Subscription> subscriptions = user.get().getSubscriptions();
		assertNotNull(subscriptions);
		assertEquals(0, subscriptions.size());
		
		subscriptionService.subscribe(user.get(), testCategory);
		
		user = userService.getUserByLoginName(existingUserName);
		subscriptions = user.get().getSubscriptions();
		assertNotNull(subscriptions);
		assertEquals(1, subscriptions.size());
	}

	@Test
	public void getSubscriptionsByUser(){
		Optional<User> user = userService.getUserByLoginName(existingUserName);
		assertNotNull(user.get());
		
		List<SubscriptionDTO> subscriptions = subscriptionService.getSubscriptionsByUser(user.get(), categoryList);
		assertNotNull(subscriptions);
		assertEquals(1, subscriptions.size());
		assertEquals(subscriptionDTO, subscriptions.get(0));
		assertEquals(subscriptionDTO.getId(), subscriptions.get(0).getId());
		assertEquals(subscriptionDTO.getName(), subscriptions.get(0).getName());
	}
	
}
