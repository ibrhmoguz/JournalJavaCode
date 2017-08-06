package com.crossover.trial.journals.notify;

/**
 * The Interface NotificationService is responsible for sending notification.
 */
public interface NotificationService {
	
	/**
	 * Send notification.
	 *
	 * @param to the to
	 * @param subject the subject
	 * @param message the message
	 */
	void notify(String to, String subject, String message);
	
}
