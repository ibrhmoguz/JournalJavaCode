package com.crossover.trial.journals.scheduler;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.crossover.trial.journals.model.Journal;
import com.crossover.trial.journals.notify.JournalNotifier;

/**
 * The Class DailySchedulerImpl.
 */
@Service
public class DailySchedulerImpl implements DailyScheduler{
	
	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(DailySchedulerImpl.class);

	/** The journal list. */
	private Set<Journal> journalList;
	
	/** The journal publish notifier. */
	private final JournalNotifier journalPublishNotifier;
	
	/**
	 * Instantiates a new daily scheduler implementation.
	 *
	 * @param journalPublishNotifier the journal publish notifier
	 */
	@Autowired
	public DailySchedulerImpl(JournalNotifier journalPublishNotifier) {
		this.journalPublishNotifier = journalPublishNotifier;
		this.journalList = new HashSet<>();
	}

	/**
	 * It notifies all users. The message contains all new journal info. 
	 * It is scheduled to be executed everyday at midnight. 
	 */
	@Scheduled(cron = "0 0 0 * * *")
	@Override
	public void notifyUsersForNewJournals() {
		StringBuilder journalInfo = new StringBuilder();
		for (Journal journal : journalList) {
			journalInfo.append(journal.toString()).append("<br>");
		}

		journalPublishNotifier.notifyAllUsers(journalInfo.toString());

		journalList.clear();

		log.info("All users are notified");
	}

	/* (non-Javadoc)
	 * @see com.crossover.trial.journals.scheduler.DailyScheduler#addJournalToBeNotified(com.crossover.trial.journals.model.Journal)
	 */
	@Override
	public void addJournalToBeNotified(Journal journal) {
		if(journalList.add(journal)){
			journalPublishNotifier.notifySubscribers(journal);
		}	
	}
	
	
}
