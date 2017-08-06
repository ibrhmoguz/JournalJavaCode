package com.crossover.trial.journals.service;

import java.util.List;

import com.crossover.trial.journals.dto.SubscriptionDTO;
import com.crossover.trial.journals.model.Category;
import com.crossover.trial.journals.model.Subscription;
import com.crossover.trial.journals.model.User;

/**
 * The Interface SubscriptionService.
 */
public interface SubscriptionService {
	
	/**
	 * Subscribe.
	 *
	 * @param user the user
	 * @param category the category
	 */
	void subscribe(User user, Category category);

	/**
	 * Gets the subscriptions by user.
	 *
	 * @param user the user
	 * @param categories the category list
	 * @return the subscriptions by user
	 */
	List<SubscriptionDTO> getSubscriptionsByUser(User user, List<Category> categories);

	/**
	 * Gets the subscriptions by category.
	 *
	 * @param category the category
	 * @return the subscriptions by category
	 */
	List<Subscription> getSubscriptionsByCategory(Category category);

	/**
	 * Gets the subscription on category.
	 *
	 * @param category the category
	 * @param subscriptions the subscriptions
	 * @return the subscription on category
	 */
	SubscriptionDTO getSubscriptionOnCategory(Category category, List<Subscription> subscriptions);

}
