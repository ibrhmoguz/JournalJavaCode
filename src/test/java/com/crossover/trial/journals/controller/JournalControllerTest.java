package com.crossover.trial.journals.controller;

import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.io.File;
import java.io.FileInputStream;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.crossover.trial.journals.Application;
import com.crossover.trial.journals.model.Journal;
import com.crossover.trial.journals.model.Publisher;
import com.crossover.trial.journals.model.User;
import com.crossover.trial.journals.repository.PublisherRepository;
import com.crossover.trial.journals.service.DirectoryService;
import com.crossover.trial.journals.service.FileService;
import com.crossover.trial.journals.service.JournalService;
import com.crossover.trial.journals.service.UserService;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class JournalControllerTest {

	private String userName = "kibarayhan@gmail.com";
	private MockMultipartFile multipartFile;	
	private String journalName = "Jornal Test";
	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PublisherRepository publisherRepository;

	@Autowired
	private DirectoryService directoryService;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private JournalService journalService;
	
	private GreenMail smtpServer;
	
	@Before
	public void setup() throws Exception {
		this.mockMvc = webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
		
		File file = new File("src/test/resources/TestFile.pdf");
		FileInputStream input = new FileInputStream(file);
		multipartFile = new MockMultipartFile("file", file.getName(), "application/pdf", IOUtils.toByteArray(input));
		input.close();
		
		smtpServer = new GreenMail(new ServerSetup(3025, "localhost", "smtp"));
		smtpServer.start();
	}
	
	@Test
	@WithMockUser(roles = "PUBLISHER", username = "kibarayhan@gmail.com", password = "Publisher1")
	public void renderDocument() throws Exception {
		String uuid = UUID.randomUUID().toString();

		Optional<User> user = userService.getUserByLoginName(userName);
		assertNotNull(user.get());
		
		Optional<Publisher> publisher = publisherRepository.findByUser(user.get());
		assertNotNull(publisher.get());
		
		File dir = new File(directoryService.getDirectoryName(publisher.get().getId()));
		directoryService.createDirectory(dir);

		fileService.uploadFile(multipartFile, publisher.get().getId(), uuid);

		Journal journal= new Journal();
		journal.setUuid(uuid);
		journal.setName(journalName);
		journalService.publish(publisher.get(), journal, 1L);

		mockMvc.perform(get("/view/"+ journal.getId())).andExpect(status().isOk()).andDo(print());
	}
	
	@After
    public void tearDown() throws Exception {
        smtpServer.stop();
    }
}
