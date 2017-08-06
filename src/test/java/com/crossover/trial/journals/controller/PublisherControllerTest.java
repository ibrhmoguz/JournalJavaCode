package com.crossover.trial.journals.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.crossover.trial.journals.Application;
import com.crossover.trial.journals.model.Journal;
import com.crossover.trial.journals.model.Publisher;
import com.crossover.trial.journals.model.User;
import com.crossover.trial.journals.repository.PublisherRepository;
import com.crossover.trial.journals.service.JournalService;
import com.crossover.trial.journals.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class PublisherControllerTest {

	private MockMvc mockMvc;
	private MockMultipartFile multipartFile;
	private MockMultipartFile emptyFile;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private FilterChainProxy springSecurityFilter;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private UserService userService;

	@Autowired
	private PublisherRepository publisherRepository;

	@Autowired
	private JournalService journalService;
	
	private String userName = "kibarayhan@gmail.com";
	private String journalName = "handleFileUpload_journalName";
	private String categoryId = "1";

	@Before
	public void setup() throws Exception {
		this.mockMvc = webAppContextSetup(webApplicationContext).addFilters(springSecurityFilter).build();

		File file = new File("src/test/resources/TestFile.pdf");
		FileInputStream input = new FileInputStream(file);
		multipartFile = new MockMultipartFile("file", file.getName(), "application/pdf", IOUtils.toByteArray(input));
		input.close();
		emptyFile = new MockMultipartFile("file", file.getName(), "application/pdf", "".getBytes());
		
	}

	@Test
	public void provideUploadInfo() throws Exception {
		MockHttpSession session = setSession(userName);
		mockMvc.perform(get("/publisher/publish").session(session)).andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void handleFileUpload_GivenEmptyFile_ShouldReturnError() throws Exception {
		MockHttpSession session = setSession(userName);

		mockMvc.perform(fileUpload("/publisher/publish").file(emptyFile).session(session).param("name", journalName)
				.param("category", categoryId)).andExpect(flash().attributeExists("message"));
	}

	@Test
	public void handleFileUpload_GivenInvalidCategoryId_ShouldReturnError() throws Exception {
		MockHttpSession session = setSession(userName);

		mockMvc.perform(fileUpload("/publisher/publish").file(multipartFile).session(session).param("name", journalName)
				.param("category", "2000")).andExpect(flash().attributeExists("message"));
	}

	@Test
	public void handleFileUpload() throws Exception {
		Optional<User> user = userService.getUserByLoginName(userName);
		assertNotNull(user.get());

		Optional<Publisher> publisher = publisherRepository.findByUser(user.get());
		assertNotNull(publisher.get());

		List<Journal> journals = journalService.publisherList(publisher.get());
		int journalsCount = journals.size();
		journals.stream().forEach(j -> assertNotEquals(journalName, j.getName()));

		MockHttpSession session = setSession(userName);

		mockMvc.perform(fileUpload("/publisher/publish").file(multipartFile).session(session).param("name", journalName)
				.param("category", categoryId)).andExpect(redirectedUrl("/publisher/browse"));

		journals = journalService.publisherList(publisher.get());
		int newJournalsCount = journals.size();
		assertEquals(journalsCount + 1, newJournalsCount);
		assertTrue(journals.stream().anyMatch(journal -> journalName.equals(journal.getName())));
	}

	protected MockHttpSession setSession(String username) {
		UsernamePasswordAuthenticationToken principal = this.getPrincipal(username);

		MockHttpSession session = new MockHttpSession();
		MockSecurityContext securityContext = new MockSecurityContext(principal);
		session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
		return session;
	}

	protected UsernamePasswordAuthenticationToken getPrincipal(String username) {
		UserDetails user = userDetailsService.loadUserByUsername(username);
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user,
				user.getPassword(), user.getAuthorities());
		return authentication;
	}

	public static class MockSecurityContext implements SecurityContext {
		private static final long serialVersionUID = -1386535243513362694L;
		private Authentication authentication;

		public MockSecurityContext(Authentication authentication) {
			this.authentication = authentication;
		}

		@Override
		public Authentication getAuthentication() {
			return this.authentication;
		}

		@Override
		public void setAuthentication(Authentication authentication) {
			this.authentication = authentication;
		}
	}

}
