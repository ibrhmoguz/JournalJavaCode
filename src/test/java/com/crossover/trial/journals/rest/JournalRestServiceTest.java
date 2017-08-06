package com.crossover.trial.journals.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.crossover.trial.journals.Application;
import com.crossover.trial.journals.model.Category;
import com.crossover.trial.journals.model.Journal;
import com.crossover.trial.journals.model.Publisher;
import com.crossover.trial.journals.model.Subscription;
import com.crossover.trial.journals.model.User;
import com.crossover.trial.journals.repository.CategoryRepository;
import com.crossover.trial.journals.repository.PublisherRepository;
import com.crossover.trial.journals.service.JournalService;
import com.crossover.trial.journals.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class JournalRestServiceTest {

	private String userName = "ayhankibar.co@gmail.com";
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private JournalService journalService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private PublisherRepository publisherRepository;
	
	private Category testCategory;

	
	@Before
	public void setup() throws Exception {
		this.mockMvc = webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
		
		Category c = new Category();
		c.setName("Test Category2");
		testCategory = categoryRepository.save(c);		
	}

	@Test
	@WithMockUser(roles = "PUBLISHER", username = "ayhankibar.co@gmail.com", password = "Publisher1")
	public void browse() throws Exception {
		mockMvc.perform(get("/rest/journals")).andExpect(status().isOk()).andDo(print());
	}

	@Test
	@WithMockUser(roles = "PUBLISHER", username = "ayhankibar.co@gmail.com", password = "Publisher1")
	public void publishedList() throws Exception {
		mockMvc.perform(get("/rest/journals/published")).andExpect(status().isOk()).andDo(print());
	}

	@Test
	@WithMockUser(roles = "PUBLISHER", username = "ayhankibar.co@gmail.com", password = "Publisher1")
	public void unPublish() throws Exception {
		Optional<User> user = userService.getUserByLoginName(userName);
		assertNotNull(user.get());

		Optional<Publisher> p = publisherRepository.findByUser(user.get());
		List<Journal> journals = journalService.publisherList(p.get());
		assertEquals(2, journals.size());
	
		Journal journalTobeDeleted = journals.get(0);

		mockMvc.perform(delete("/rest/journals/unPublish/" + journalTobeDeleted.getId())).andExpect(status().isOk())
				.andDo(print());

		journals = journalService.publisherList(p.get());
		journals.stream().forEach(j -> assertNotEquals(journalTobeDeleted.getId(), j.getId()));
		journals.stream().forEach(j -> assertNotEquals(journalTobeDeleted, j));
		
	}

	@Test
	@WithMockUser(roles = "PUBLISHER", username = "ayhankibar.co@gmail.com", password = "Publisher1")
	public void getUserSubscriptions() throws Exception {
		mockMvc.perform(get("/rest/journals/subscriptions")).andExpect(status().isOk()).andDo(print());
	}

	@Test
	@WithMockUser(roles = "PUBLISHER", username = "ayhankibar.co@gmail.com", password = "Publisher1")
	public void subscribe() throws Exception {
		Optional<User> user = userService.getUserByLoginName(userName);
		assertNotNull(user.get());
		
		List<Subscription> subscriptions = user.get().getSubscriptions();
		assertNotNull(subscriptions);
		assertEquals(0, subscriptions.size());
		
		mockMvc.perform(post("/rest/journals/subscribe/"+testCategory.getId())).andExpect(status().isOk())
				.andDo(print());
		
		user = userService.getUserByLoginName(userName);
		subscriptions = user.get().getSubscriptions();
		assertNotNull(subscriptions);
		assertEquals(1, subscriptions.size());
		assertTrue(subscriptions.get(0).getCategory().equals(testCategory));
	}

}
