package com.crossover.trial.journals.notify;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.crossover.trial.journals.dto.EmailDto;
import com.crossover.trial.journals.model.Journal;
import com.crossover.trial.journals.model.Subscription;
import com.crossover.trial.journals.model.User;
import com.crossover.trial.journals.service.SubscriptionService;
import com.crossover.trial.journals.service.UserService;

/**
 * The Class JournalNotifierImpl.
 */
@Service
public class JournalNotifierImpl implements JournalNotifier {

	private static final String JOURNAL_SUBJECT = "New Journal is available.";

	@Value("${queue.new.journal}")
	private String journalQueueName;
	
	/** The subscription service. */
	private final SubscriptionService subscriptionService;

	/** The user service. */
	private final UserService userService;

	private final JmsTemplate jmsTemplate;

	/**
	 * Instantiates a new journal publish notifier implementation.
	 *
	 * @param subscriptionService
	 *            the subscription service
	 * @param userService
	 *            the user service
	 * @param jmsTemplate
	 *            the jms template
	 */
	@Autowired
	public JournalNotifierImpl(SubscriptionService subscriptionService, UserService userService,
			JmsTemplate jmsTemplate) {
		this.subscriptionService = subscriptionService;
		this.userService = userService;
		this.jmsTemplate = jmsTemplate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.crossover.trial.journals.notify.JournalPublishNotifier#
	 * notifySubscribers(com.crossover.trial.journals.model.Journal)
	 */
	@Override
	public void notifySubscribers(Journal journal) {
		List<Subscription> subscriptions = subscriptionService.getSubscriptionsByCategory(journal.getCategory());
		for (Subscription subscription : subscriptions) {
			EmailDto emailDto = new EmailDto(subscription.getUser().getLoginName(), JOURNAL_SUBJECT,
					journal.toString());
			jmsTemplate.convertAndSend(journalQueueName, emailDto);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.crossover.trial.journals.notify.JournalPublishNotifier#notifyAllUsers
	 * (com.crossover.trial.journals.model.Journal)
	 */
	@Async
	@Override
	public void notifyAllUsers(String message) {
		List<User> users = userService.findAll();
		for (User user : users) {
			EmailDto emailDto = new EmailDto(user.getLoginName(), JOURNAL_SUBJECT, message);
			jmsTemplate.convertAndSend(journalQueueName, emailDto);
		}
	}

}
