package com.crossover.trial.journals.controller;

import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.crossover.trial.journals.model.Journal;
import com.crossover.trial.journals.model.Publisher;
import com.crossover.trial.journals.model.User;
import com.crossover.trial.journals.repository.PublisherRepository;
import com.crossover.trial.journals.service.FileService;
import com.crossover.trial.journals.service.JournalService;
import com.crossover.trial.journals.service.UserService;

@Controller
public class PublisherController {

	private static final String PUBLISHER_PUBLISH = "publisher/publish";

	private static final Logger LOGGER = Logger.getLogger(PublisherController.class);

	private PublisherRepository publisherRepository;

	private JournalService journalService;

	private FileService fileService;

	private UserService userService;

	@Autowired
	public PublisherController(PublisherRepository publisherRepository, JournalService journalService,
			FileService fileService, UserService userService) {
		this.publisherRepository = publisherRepository;
		this.journalService = journalService;
		this.fileService = fileService;
		this.userService = userService;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/publisher/publish")
	public String provideUploadInfo(Model model) {
		return PUBLISHER_PUBLISH;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/publisher/publish")
	@PreAuthorize("hasRole('PUBLISHER')")
	public String handleFileUpload(@RequestParam("name") String name, @RequestParam("category") Long categoryId,
			@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes,
			@AuthenticationPrincipal Principal principal) {

		Optional<User> user = userService.getUserByLoginName(principal.getName());
		
		Optional<Publisher> publisher = publisherRepository.findByUser(user.get());

		if (!file.isEmpty()) {
			try {
				String uuid = UUID.randomUUID().toString();

				fileService.uploadFile(file, publisher.get().getId(), uuid);

				Journal journal = new Journal();
				journal.setUuid(uuid);
				journal.setName(name);
				
				journalService.publish(publisher.get(), journal, categoryId);
				
				return "redirect:/publisher/browse";
			} catch (Exception e) {
				LOGGER.error("You failed to publish " + name, e);
				redirectAttributes.addFlashAttribute("message",
						"You failed to publish " + name + " => " + e.getMessage());
			}
		} else {
			redirectAttributes.addFlashAttribute("message",
					"You failed to upload " + name + " because the file was empty");
		}

		return "redirect:/publisher/publish";
	}

}