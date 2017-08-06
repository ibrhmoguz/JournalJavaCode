package com.crossover.trial.journals.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.crossover.trial.journals.dto.SubscriptionDTO;
import com.crossover.trial.journals.model.Journal;
import com.crossover.trial.journals.model.User;
import com.crossover.trial.journals.service.FileService;
import com.crossover.trial.journals.service.JournalService;
import com.crossover.trial.journals.service.SubscriptionService;
import com.crossover.trial.journals.service.UserService;

@Controller
public class JournalController {

	private SubscriptionService subscriptionService;

	private UserService userService;

	private FileService fileService;

	private JournalService journalService;
	
	@Autowired
	public JournalController(SubscriptionService subscriptionService, UserService userService, FileService fileService,
			JournalService journalService) {
		this.subscriptionService = subscriptionService;
		this.userService = userService;
		this.fileService = fileService;
		this.journalService = journalService;
	}

	@ResponseBody
	@RequestMapping(value = "/view/{id}", method = RequestMethod.GET, produces = "application/pdf")
	public ResponseEntity renderDocument(@AuthenticationPrincipal Principal principal, @PathVariable("id") Long id)
			throws IOException {
		
		Journal journal = journalService.findOne(id);
		
		Optional<User> user = userService.getUserByLoginName(principal.getName());
		SubscriptionDTO subscription = subscriptionService.getSubscriptionOnCategory(journal.getCategory(), 
				user.get().getSubscriptions());

		if (subscription.isActive() || journalService.isJournalPublisherEqualsToUser(journal, user.get()) ) {
			return ResponseEntity.ok(fileService.getFile(journal));
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
