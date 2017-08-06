package com.crossover.trial.journals.service;

import java.util.List;
import java.util.Optional;

import com.crossover.trial.journals.model.User;

/**
 * The Interface UserService.
 */
public interface UserService {

    /**
     * Gets the user by login name.
     *
     * @param loginName the login name
     * @return the user by login name
     */
    Optional<User> getUserByLoginName(String loginName);

	/**
	 * Find all.
	 *
	 * @return the list
	 */
	List<User> findAll();

	/**
	 * Save.
	 *
	 * @param user the user
	 * @return the user
	 */
	User save(User user);
	
}