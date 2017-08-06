package com.crossover.trial.journals.service;

import java.io.File;

import com.crossover.trial.journals.service.exception.DirectoryCannotCreatedException;

/**
 * The Interface DirectoryService.
 */
public interface DirectoryService {

	/**
	 * Gets the directory name.
	 *
	 * @param publisherId the publisher id
	 * @return the directory name
	 */
	String getDirectoryName(long publisherId);

	/**
	 * Creates the directory.
	 *
	 * @param dir the dir
	 * @throws DirectoryCannotCreatedException the directory cannot created exception
	 */
	void createDirectory(File dir) throws DirectoryCannotCreatedException;

}
