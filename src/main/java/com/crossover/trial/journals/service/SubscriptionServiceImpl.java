package com.crossover.trial.journals.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crossover.trial.journals.dto.SubscriptionDTO;
import com.crossover.trial.journals.model.Category;
import com.crossover.trial.journals.model.Subscription;
import com.crossover.trial.journals.model.User;
import com.crossover.trial.journals.repository.SubscriptionRepository;

/**
 * The Class SubscriptionServiceImpl.
 */
@Service
public class SubscriptionServiceImpl implements SubscriptionService {

	/** The subscription repository. */
	private final SubscriptionRepository subscriptionRepository;

	/**
	 * Instantiates a new subscription service implementation.
	 *
	 * @param subscriptionRepository the subscription repository
	 */
	@Autowired
	public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository) {
		this.subscriptionRepository = subscriptionRepository;
	}

	/* (non-Javadoc)
	 * @see com.crossover.trial.journals.service.SubscriptionService#subscribe(com.crossover.trial.journals.model.User, com.crossover.trial.journals.model.Category)
	 */
	@Override
	public void subscribe(User user, Category category) {
		List<Subscription> subscriptions = user.getSubscriptions();

		Optional<Subscription> subscr = subscriptions.stream().filter(s -> s.getCategory().equals(category))
				.findFirst();

		if (!subscr.isPresent()) {
			Subscription subscription = new Subscription();
			subscription.setUser(user);
			subscription.setCategory(category);
			subscriptionRepository.save(subscription);
		}
	}

	/* (non-Javadoc)
	 * @see com.crossover.trial.journals.service.SubscriptionService#getSubscriptionsByUser(com.crossover.trial.journals.model.User, java.util.List)
	 */
	@Override
	public List<SubscriptionDTO> getSubscriptionsByUser(User user, List<Category> categories) {
		List<Subscription> subscriptions = user.getSubscriptions();

		List<SubscriptionDTO> subscriptionDTOs = new ArrayList<>(categories.size());
		categories.stream().forEach(c -> subscriptionDTOs.add(getSubscriptionOnCategory(c, subscriptions)) );
		return subscriptionDTOs;
	}

	/* (non-Javadoc)
	 * @see com.crossover.trial.journals.service.SubscriptionService#getSubscriptionsByCategory(com.crossover.trial.journals.model.Category)
	 */
	@Override
	public List<Subscription> getSubscriptionsByCategory(Category category) {
		return subscriptionRepository.findSubscriptionsByCategory(category);
	}

	/* (non-Javadoc)
	 * @see com.crossover.trial.journals.service.SubscriptionService#getSubscriptionOnCategory(com.crossover.trial.journals.model.Category, java.util.List)
	 */
	@Override
	public SubscriptionDTO getSubscriptionOnCategory(Category category, List<Subscription> subscriptions) {
		SubscriptionDTO subscr = new SubscriptionDTO(category);
		Optional<Subscription> subscription = subscriptions.stream().filter(s -> s.getCategory().equals(category))
				.findFirst();
		subscr.setActive(subscription.isPresent());
		return subscr;
	}
	
}
