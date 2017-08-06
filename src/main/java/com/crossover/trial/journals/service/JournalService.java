package com.crossover.trial.journals.service;

import java.util.List;

import com.crossover.trial.journals.model.Journal;
import com.crossover.trial.journals.model.Publisher;
import com.crossover.trial.journals.model.User;
import com.crossover.trial.journals.service.exception.CategoryNotFoundException;
import com.crossover.trial.journals.service.exception.JournalException;

/**
 * The Interface JournalService.
 */
public interface JournalService {

	/**
	 * Retrieves all journals of a given user.
	 *
	 * @param user the user
	 * @return the list
	 */
	List<Journal> listAll(User user);

	/**
	 * Retrieves all journals of a given publisher.
	 *
	 * @param publisher the publisher
	 * @return the list
	 */
	List<Journal> publisherList(Publisher publisher);

	/**
	 * Publishes a given journal related with publisher and category.
	 *
	 * @param publisher the publisher
	 * @param journal the journal
	 * @param categoryId the category id
	 * @return the journal
	 * @throws CategoryNotFoundException the category not found exception
	 */
	Journal publish(Publisher publisher, Journal journal, Long categoryId) throws CategoryNotFoundException;

	/**
	 * Publishes a given journal related with publisher.
	 *
	 * @param publisher the publisher
	 * @param journalId the journal id
	 * @throws JournalException the journal exception
	 */
	void unPublish(Publisher publisher, Long journalId) throws JournalException;

	/**
	 * Find one.
	 *
	 * @param id the id
	 * @return the journal
	 */
	Journal findOne(Long id);

	/**
	 * Checks if is journal publisher equals to user.
	 *
	 * @param journal the journal
	 * @param user the user
	 * @return true, if is journal publisher equals to user
	 */
	boolean isJournalPublisherEqualsToUser(Journal journal, User user);

}
