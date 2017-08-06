package com.crossover.trial.journals.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crossover.trial.journals.model.Publisher;
import com.crossover.trial.journals.model.User;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {

	Optional<Publisher> findByUser(User user);

}
