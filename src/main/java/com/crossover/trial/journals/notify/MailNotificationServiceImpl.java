package com.crossover.trial.journals.notify;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Service for sending e-mails. The @Async annotation is used to send e-mails
 * asynchronously.
 */
@Service
public final class MailNotificationServiceImpl implements NotificationService{

	private static final String FROM = "ayhankibar.co@gmail.com";

	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(MailNotificationServiceImpl.class);

	/** The java mail sender. */
	private final JavaMailSender javaMailSender;

	/** The Message builder in order to be used by different template engines. */
	private final MessageBuilder messageBuilder;

	/**
	 * Instantiates a new mail notification service impl.
	 *
	 * @param javaMailSender the java mail sender
	 * @param messageBuilder the message builder
	 */
	@Autowired
	public MailNotificationServiceImpl(JavaMailSender javaMailSender, MessageBuilder messageBuilder) {
		this.javaMailSender = javaMailSender;
		this.messageBuilder = messageBuilder;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.crossover.trial.journals.notify.NotificationService#sendNotification(
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	@Async
	@Override
	public void notify(String to, String subject, String message){
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mailMessage = new MimeMessageHelper(mimeMessage);
            mailMessage.setFrom(FROM);
            mailMessage.setTo(to);
            mailMessage.setSubject(subject);
            mailMessage.setText(messageBuilder.build(message), true);
            javaMailSender.send(mimeMessage);
            log.debug("Sent email to User '{}'", to);
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.warn("Email could not be sent to user '{}'", to, e);
            } else {
                log.warn("Email could not be sent to user '{}': {}", to, e.getMessage());
            }
        }		
		
	}

}
