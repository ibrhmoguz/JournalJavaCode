package com.crossover.trial.journals.service.exception;

import java.io.IOException;

/**
 * The Class DirectoryCannotCreatedException.
 */
public class DirectoryCannotCreatedException extends IOException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8566901433320015326L;

	/**
	 * Instantiates a new directory cannot created exception.
	 *
	 * @param message the message
	 */
	public DirectoryCannotCreatedException(String message){
		 super(message);
	 }
	
}
