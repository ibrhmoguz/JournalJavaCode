package com.crossover.trial.journals.service.exception;

/**
 * The Class CategoryNotFoundException.
 */
public class CategoryNotFoundException extends Exception {

	private static final long serialVersionUID = -2783771751228250207L;

	/**
	 * Instantiates a new category not found exception.
	 *
	 * @param message the message
	 */
	public CategoryNotFoundException(String message) {
		super(message);
	}

}
