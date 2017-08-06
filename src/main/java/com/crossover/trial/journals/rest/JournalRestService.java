package com.crossover.trial.journals.rest;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.crossover.trial.journals.dto.SubscriptionDTO;
import com.crossover.trial.journals.model.Category;
import com.crossover.trial.journals.model.Journal;
import com.crossover.trial.journals.model.Publisher;
import com.crossover.trial.journals.model.User;
import com.crossover.trial.journals.repository.PublisherRepository;
import com.crossover.trial.journals.service.CategoryService;
import com.crossover.trial.journals.service.JournalService;
import com.crossover.trial.journals.service.SubscriptionService;
import com.crossover.trial.journals.service.UserService;
import com.crossover.trial.journals.service.exception.CategoryNotFoundException;
import com.crossover.trial.journals.service.exception.JournalException;

@RestController
@RequestMapping("/rest/journals")
public class JournalRestService {

	@Autowired
	private PublisherRepository publisherRepository;

	@Autowired
	private JournalService journalService;

	@Autowired
	private UserService userService;

	@Autowired
	private SubscriptionService subscriptionService;

	@Autowired
	private CategoryService categoryService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<Object> browse(@AuthenticationPrincipal Principal principal) {
		Optional<User> user = userService.getUserByLoginName(principal.getName());
		return ResponseEntity.ok(journalService.listAll(user.get()));
	}

	@RequestMapping(value = "/published", method = RequestMethod.GET)
	public List<Journal> publishedList(@AuthenticationPrincipal Principal principal) {
		Optional<User> user = userService.getUserByLoginName(principal.getName());
		Optional<Publisher> publisher = publisherRepository.findByUser(user.get());
		return journalService.publisherList(publisher.get());
	}

	@RequestMapping(value = "/unPublish/{id}", method = RequestMethod.DELETE)
	public void unPublish(@PathVariable("id") Long id, @AuthenticationPrincipal Principal principal)
			throws JournalException {
		Optional<User> user = userService.getUserByLoginName(principal.getName());
		Optional<Publisher> publisher = publisherRepository.findByUser(user.get());
		journalService.unPublish(publisher.get(), id);
	}

	@RequestMapping(value = "/subscriptions")
	public List<SubscriptionDTO> getUserSubscriptions(@AuthenticationPrincipal Principal principal) {
		Optional<User> user = userService.getUserByLoginName(principal.getName());
		return subscriptionService.getSubscriptionsByUser(user.get(), categoryService.findAll());
	}

	/**
	 * @param categoryId
	 * @param principal
	 * @throws CategoryNotFoundException
	 */
	@RequestMapping(value = "/subscribe/{categoryId}", method = RequestMethod.POST)
	public void subscribe(@PathVariable("categoryId") Long categoryId, @AuthenticationPrincipal Principal principal)
			throws CategoryNotFoundException {
		Optional<User> user = userService.getUserByLoginName(principal.getName());
		Category category = categoryService.findByCategoryId(categoryId);
		subscriptionService.subscribe(user.get(), category);
	}
	
}
