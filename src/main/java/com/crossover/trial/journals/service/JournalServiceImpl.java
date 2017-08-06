package com.crossover.trial.journals.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crossover.trial.journals.model.Category;
import com.crossover.trial.journals.model.Journal;
import com.crossover.trial.journals.model.Publisher;
import com.crossover.trial.journals.model.Subscription;
import com.crossover.trial.journals.model.User;
import com.crossover.trial.journals.repository.JournalRepository;
import com.crossover.trial.journals.scheduler.DailyScheduler;
import com.crossover.trial.journals.service.exception.CategoryNotFoundException;
import com.crossover.trial.journals.service.exception.JournalException;

/**
 * The Class JournalServiceImpl.
 */
@Service
public class JournalServiceImpl implements JournalService {

	/** The journal repository. */
	private final JournalRepository journalRepository;

	/** The category service. */
	private final CategoryService categoryService;

	/** The file service. */
	private final FileService fileService;

	/** The scheduler. */
	private final DailyScheduler scheduler;

	/**
	 * Instantiates a new journal service implementation.
	 *
	 * @param journalRepository the journal repository
	 * @param userService the user service
	 * @param categoryService the category service
	 * @param fileService the file service
	 * @param scheduler the scheduler
	 */
	@Autowired
	public JournalServiceImpl(JournalRepository journalRepository, CategoryService categoryService, 
			FileService fileService, DailyScheduler scheduler) {
		this.journalRepository = journalRepository;
		this.categoryService = categoryService;
		this.fileService = fileService;
		this.scheduler = scheduler;
	}

	/* (non-Javadoc)
	 * @see com.crossover.trial.journals.service.JournalService#listAll(com.crossover.trial.journals.model.User)
	 */
	@Override
	public List<Journal> listAll(User user) {
		List<Subscription> subscriptions = user.getSubscriptions();
		if (!subscriptions.isEmpty()) {
			List<Long> ids = new ArrayList<>(subscriptions.size());
			subscriptions.stream().forEach(s -> ids.add(s.getCategory().getId()));
			return journalRepository.findByCategoryIdIn(ids);
		} else {
			return Collections.emptyList();
		}
	}

	/* (non-Javadoc)
	 * @see com.crossover.trial.journals.service.JournalService#publisherList(com.crossover.trial.journals.model.Publisher)
	 */
	@Override
	public List<Journal> publisherList(Publisher publisher) {
		Iterable<Journal> journals = journalRepository.findByPublisher(publisher);
		return StreamSupport.stream(journals.spliterator(), false).collect(Collectors.toList());
	}

	/* (non-Javadoc)
	 * @see com.crossover.trial.journals.service.JournalService#publish(com.crossover.trial.journals.model.Publisher, com.crossover.trial.journals.model.Journal, java.lang.Long)
	 */
	@Override
	public Journal publish(Publisher publisher, Journal journal, Long categoryId) throws CategoryNotFoundException {
		Category category = categoryService.findByCategoryId(categoryId);
		journal.setPublisher(publisher);
		journal.setCategory(category);
		Journal persistedJournal = journalRepository.save(journal);
		scheduler.addJournalToBeNotified(persistedJournal);
		return persistedJournal;
	}

	/* (non-Javadoc)
	 * @see com.crossover.trial.journals.service.JournalService#unPublish(com.crossover.trial.journals.model.Publisher, java.lang.Long)
	 */
	@Override
	public void unPublish(Publisher publisher, Long id) throws JournalException {
		Journal journal = journalRepository.findOne(id);
		if (journal == null) {
			throw new JournalException("Journal with id: " + id + " doesn't exist.");
		}

		if (!journal.getPublisher().equals(publisher)) {
			throw new JournalException(journal + " cannot be removed.");
		}

		fileService.deleteFile(publisher.getId(), journal.getUuid());

		journalRepository.delete(journal);
	}

	/* (non-Javadoc)
	 * @see com.crossover.trial.journals.service.JournalService#findOne(java.lang.Long)
	 */
	@Override
	public Journal findOne(Long id) {
		return journalRepository.findOne(id);
	}

	/* (non-Javadoc)
	 * @see com.crossover.trial.journals.service.JournalService#isJournalPublisherEqualsToUser(com.crossover.trial.journals.model.Journal, com.crossover.trial.journals.model.User)
	 */
	@Override
	public boolean isJournalPublisherEqualsToUser(Journal journal, User user) {
		return journal.getPublisher().getId().equals(user.getId());
	}

}
