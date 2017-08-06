package com.crossover.trial.journals.scheduler;

import com.crossover.trial.journals.model.Journal;

/**
 * The Interface DailyScheduler.
 */
public interface DailyScheduler{
	
	/**
	 * Adds the journal to be notified.
	 *
	 * @param journal the journal
	 */
	void addJournalToBeNotified(Journal journal);
	
	/**
	 * Notify users for all journals.
	 */
	void notifyUsersForNewJournals();
}
