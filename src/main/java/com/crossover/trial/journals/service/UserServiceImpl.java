package com.crossover.trial.journals.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crossover.trial.journals.model.User;
import com.crossover.trial.journals.repository.UserRepository;

/**
 * The Class UserServiceImpl.
 */
@Service
public class UserServiceImpl implements UserService {

	/** The user repository. */
	private UserRepository userRepository;

	/**
	 * Instantiates a new user service impl.
	 *
	 * @param userRepository the user repository
	 */
	@Autowired
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/* (non-Javadoc)
	 * @see com.crossover.trial.journals.service.UserService#getUserByLoginName(java.lang.String)
	 */
	@Override
	public Optional<User> getUserByLoginName(String loginName) {
		return Optional.ofNullable(userRepository.findByLoginName(loginName));
	}

	/* (non-Javadoc)
	 * @see com.crossover.trial.journals.service.UserService#findAll()
	 */
	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

	/* (non-Javadoc)
	 * @see com.crossover.trial.journals.service.UserService#save(com.crossover.trial.journals.model.User)
	 */
	@Override
	public User save(User user) {
		return userRepository.save(user);
	}

}
