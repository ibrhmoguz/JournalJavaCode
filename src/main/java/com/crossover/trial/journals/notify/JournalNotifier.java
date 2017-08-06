package com.crossover.trial.journals.notify;

import com.crossover.trial.journals.model.Journal;

/**
 * The Interface JournalNotifier which provides notification methods.
 */
public interface JournalNotifier {
	
	/**
	 * Notify subscribers.
	 *
	 * @param journal the journal
	 */
	void notifySubscribers(Journal journal);

	/**
	 * Notify all users.
	 *
	 * @param message contains all journal's information
	 */
	void notifyAllUsers(String message);
	
}
