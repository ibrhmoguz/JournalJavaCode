package com.crossover.trial.journals.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.crossover.trial.journals.service.exception.DirectoryCannotCreatedException;

/**
 * The Class DirectoryServiceImpl.
 */
@Service
public class DirectoryServiceImpl implements DirectoryService {

	/** The upload directory. */
	@Value("${upload.directory}")
	private String uploadDirectory;

	/* (non-Javadoc)
	 * @see com.crossover.trial.journals.service.DirectoryService#getDirectoryName(long)
	 */
	@Override
	public String getDirectoryName(long publisherId) {
		return uploadDirectory + "/" + publisherId;
	}

	/* (non-Javadoc)
	 * @see com.crossover.trial.journals.service.DirectoryService#createDirectory(java.io.File)
	 */
	@Override
	public void createDirectory(File dir) throws DirectoryCannotCreatedException {
		if (!dir.exists() && !dir.mkdirs()) {
			throw new DirectoryCannotCreatedException("Directory " + dir.getAbsolutePath() + "can not be created.");
		}
	}

}
