package com.crossover.trial.journals.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.crossover.trial.journals.model.Journal;
import com.crossover.trial.journals.service.exception.DirectoryCannotCreatedException;

/**
 * The Interface FileService.
 */
public interface FileService {
	
	/**
	 * Gets the file name.
	 *
	 * @param publisherId the publisher id
	 * @param uuid the external id
	 * @return the file name
	 */
	String getFileName(long publisherId, String uuid);

	/**
	 * Upload file.
	 *
	 * @param file the file
	 * @param publisherId the publisher id
	 * @param uuid the uuid
	 * @throws DirectoryCannotCreatedException 
	 */
	void uploadFile(MultipartFile file, Long publisherId, String uuid) throws DirectoryCannotCreatedException;

	/**
	 * Deletes a file.
	 *
	 * @param publisherId the publisher id
	 * @param journalUuid the journal external id
	 */
	void deleteFile(Long publisherId, String journalUuid);

	/**
	 * Gets the file.
	 *
	 * @param journal the journal
	 * @return the file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	byte[] getFile(Journal journal) throws IOException;
	
}
