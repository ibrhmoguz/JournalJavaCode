package com.crossover.trial.journals.notify.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.crossover.trial.journals.dto.EmailDto;
import com.crossover.trial.journals.notify.NotificationService;

@Component
public class EmailQueueListener{

	private static Logger logger = LoggerFactory.getLogger(EmailQueueListener.class);

	/** The notification service. */
	private final NotificationService notificationService;

	@Autowired
	public EmailQueueListener(NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	@JmsListener(destination = "${queue.new.journal}")
	public void sendNotification(EmailDto emailDto) {
		logger.info("Sending email to [" + emailDto.getTo() + "] subject [" + emailDto.getSubject() + "]");
		notificationService.notify(emailDto.getTo(), emailDto.getSubject(), emailDto.getBody());
	}

}
