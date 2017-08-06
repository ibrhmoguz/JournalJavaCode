package com.crossover.trial.journals.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.After;
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
import com.crossover.trial.journals.model.Journal;
import com.crossover.trial.journals.model.Publisher;
import com.crossover.trial.journals.model.User;
//import com.crossover.trial.journals.notify.JournalPublishNotifier;
import com.crossover.trial.journals.repository.PublisherRepository;
import com.crossover.trial.journals.scheduler.DailyScheduler;
import com.crossover.trial.journals.service.exception.CategoryNotFoundException;
import com.crossover.trial.journals.service.exception.JournalException;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JournalServiceTest {
	
	private GreenMail smtpServer;
	
	private final static String NEW_JOURNAL_NAME = "New Journal";
	private final static String USER_NAME = "ayhankibar@gmail.com";
	private final static String PUBLISHER_USER_NAME = "kibarayhan@gmail.com";
	private final static String user2Name = "user2@gmail.com";
	private final static String publisher2Name = "publisher2@gmail.com";

	@Autowired
	private JournalService journalService;

	@Autowired
	private UserService userService;

	@Autowired
	private PublisherRepository publisherRepository;

	@Autowired
	private DailyScheduler dailyScheduler;

	@Before
	public void setUp() throws Exception {
		smtpServer = new GreenMail(new ServerSetup(3025, "localhost", "smtp"));
		smtpServer.start();
	}
	
	@Test
	public void browseSubscribedUser() {
		List<Journal> journals = journalService.listAll(getUser(USER_NAME));
		assertNotNull(journals);
		assertEquals(1, journals.size());

		assertEquals(new Long(1), journals.get(0).getId());
		assertEquals("Medicine", journals.get(0).getName());
		assertEquals(new Long(1), journals.get(0).getPublisher().getId());
		assertNotNull(journals.get(0).getPublishDate());
	}

	@Test
	public void browseUnSubscribedUser() {
		List<Journal> journals = journalService.listAll(getUser(user2Name));
		assertEquals(0, journals.size());
	}

	@Test
	public void listPublisher() {
		User user = getUser(PUBLISHER_USER_NAME);
		Optional<Publisher> p = publisherRepository.findByUser(user);
		List<Journal> journals = journalService.publisherList(p.get());
		assertEquals(2, journals.size());

		assertEquals(new Long(1), journals.get(0).getId());
		assertEquals(new Long(2), journals.get(1).getId());

		assertEquals("Medicine", journals.get(0).getName());
		assertEquals("Test Journal", journals.get(1).getName());
		journals.stream().forEach(j -> assertNotNull(j.getPublishDate()));
		journals.stream().forEach(j -> assertEquals(new Long(1), j.getPublisher().getId()));

	}

	@Test
	public void publishFail() throws CategoryNotFoundException {
		User user = getUser(publisher2Name);
		Optional<Publisher> p = publisherRepository.findByUser(user);

		Journal journal = new Journal();
		journal.setName("New Journal");

		assertThatThrownBy(() -> journalService.publish(p.get(), journal, 150L))
				.isInstanceOf(CategoryNotFoundException.class).hasMessage("Category with Id 150 not found.");
	}

	@Test()
	public void publishSuccess() throws CategoryNotFoundException {
		User user = getUser(publisher2Name);
		Optional<Publisher> p = publisherRepository.findByUser(user);

		Journal journal = new Journal();
		journal.setName(NEW_JOURNAL_NAME);
		journal.setUuid("SOME_EXTERNAL_ID");
		journalService.publish(p.get(), journal, 3L);

		List<Journal> journals = journalService.listAll(getUser(USER_NAME));
		assertEquals(2, journals.size());

		journals = journalService.publisherList(p.get());
		assertEquals(2, journals.size());
		assertEquals(new Long(3), journals.get(0).getId());
		assertEquals(new Long(6), journals.get(1).getId());
		assertEquals("Health", journals.get(0).getName());
		assertEquals(NEW_JOURNAL_NAME, journals.get(1).getName());
		journals.stream().forEach(j -> assertNotNull(j.getPublishDate()));
		journals.stream().forEach(j -> assertEquals(new Long(2), j.getPublisher().getId()));

		dailyScheduler.notifyUsersForNewJournals();

		assertTrue(journals.stream().anyMatch(j -> journal.equals(j)));
	}

	@Test
	public void unPublishFail() throws JournalException {
		User user = getUser(PUBLISHER_USER_NAME);
		Optional<Publisher> p = publisherRepository.findByUser(user);

		assertThatThrownBy(() -> journalService.unPublish(p.get(), 4L)).isInstanceOf(JournalException.class);
	}

	@Test
	public void unPublishFail2() throws JournalException {
		User user = getUser(PUBLISHER_USER_NAME);
		Optional<Publisher> p = publisherRepository.findByUser(user);

		assertThatThrownBy(() -> journalService.unPublish(p.get(), 100L)).isInstanceOf(JournalException.class);
	}

	@Test
	public void unPublishFail3() throws JournalException {
		User user = getUser(PUBLISHER_USER_NAME);
		Optional<Publisher> p = publisherRepository.findByUser(user);
		
		assertThatThrownBy(() -> journalService.unPublish(p.get(), 5L)).isInstanceOf(JournalException.class);
	}

	@Test
	public void unPublishSuccess() throws JournalException {
		User user = getUser(publisher2Name);
		Optional<Publisher> p = publisherRepository.findByUser(user);
		journalService.unPublish(p.get(), 3L);

		List<Journal> journals = journalService.publisherList(p.get());
		assertEquals(1, journals.size());

		journals = journalService.listAll(getUser(USER_NAME));
		assertEquals(2, journals.size());
	}

	@Test
	public void isJournalPublisherEqualsToUser(){
		User user = getUser(publisher2Name);
		Optional<Publisher> p = publisherRepository.findByUser(user);
		List<Journal> journals = journalService.publisherList(p.get());
		assertNotNull(journals);
		assertTrue(journals.size()>0);
		
		assertTrue(journalService.isJournalPublisherEqualsToUser(journals.get(0), user));

		user = getUser(USER_NAME);
		assertFalse(journalService.isJournalPublisherEqualsToUser(journals.get(0), user));
	}
	
	protected User getUser(String name) {
		Optional<User> user = userService.getUserByLoginName(name);
		return user.get();
	}
	
	@After
    public void tearDown() throws Exception {
        smtpServer.stop();
    }

}
