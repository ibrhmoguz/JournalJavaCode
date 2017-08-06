package com.crossover.trial.journals.notify;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import javax.mail.internet.MimeMessage;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.crossover.trial.journals.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MailNotificationServiceTest {

	private MailNotificationServiceImpl notificationService;

	@Spy
	private JavaMailSenderImpl javaMailSender;

	@Captor
	private ArgumentCaptor<MimeMessage> messageCaptor;

	@Autowired
	private MessageBuilder messageBuilder;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		doNothing().when(javaMailSender).send(any(MimeMessage.class));
		notificationService = new MailNotificationServiceImpl(javaMailSender, messageBuilder);
	}

	@Test
	public void sendMailMessage_ShouldSucess() throws Exception {
		// given
		String message = "test is sucess.";
		String to = "kibarayhan2@gmail.com";
		// when
		notificationService.notify(to, message, message);
		verify(javaMailSender).send(messageCaptor.capture());

		MimeMessage expectedMesage = (MimeMessage) messageCaptor.getValue();
		assertThat(expectedMesage.getSubject()).isEqualTo(message);
		assertThat(expectedMesage.getAllRecipients()[0].toString()).isEqualTo(to);
		assertThat(expectedMesage.getContent()).isInstanceOf(String.class);
		assertThat(expectedMesage.getContent().toString()).contains(message);
		assertThat(expectedMesage.getDataHandler().getContentType()).isEqualTo("text/html");
	}

	@Test
	public void testSendEmailWithException() throws Exception {
		// given
		String message = "test is sucess.";
		String to = "kibarayhan2@gmail.com";
		// when
		doThrow(MailSendException.class).when(javaMailSender).send(any(MimeMessage.class));
		notificationService.notify(to, message, message);
	}

}
