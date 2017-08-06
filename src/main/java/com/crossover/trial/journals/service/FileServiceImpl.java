package com.crossover.trial.journals.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.crossover.trial.journals.model.Journal;
import com.crossover.trial.journals.service.exception.DirectoryCannotCreatedException;

/**
 * The Class FileServiceImpl provides file related operations.
 */
@Service
public class FileServiceImpl implements FileService {

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(FileServiceImpl.class);

	/** The directory service. */
	private DirectoryService directoryService;

	/**
	 * Instantiates a new file service impl.
	 *
	 * @param directoryService
	 *            the directory service
	 */
	@Autowired
	public FileServiceImpl(DirectoryService directoryService) {
		this.directoryService = directoryService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.crossover.trial.journals.service.FileService#getFileName(long,
	 * java.lang.String)
	 */
	@Override
	public String getFileName(long publisherId, String uuid) {
		return directoryService.getDirectoryName(publisherId) + "/" + uuid + ".pdf";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.crossover.trial.journals.service.FileService#uploadFile(org.
	 * springframework.web.multipart.MultipartFile, java.lang.Long,
	 * java.lang.String)
	 */
	@Override
	public void uploadFile(MultipartFile file, Long publisherId, String uuid) throws DirectoryCannotCreatedException {

		File dir = new File(directoryService.getDirectoryName(publisherId));
		directoryService.createDirectory(dir);

		File f = new File(getFileName(publisherId, uuid));
		try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(f))) {
			FileCopyUtils.copy(file.getInputStream(), stream);
			stream.close();
		} catch (IOException e) {
			throw (DirectoryCannotCreatedException) e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.crossover.trial.journals.service.FileService#deleteFile(java.lang.
	 * Long, java.lang.String)
	 */
	@Override
	public void deleteFile(Long publisherId, String journalUuid) {
		String filePath = getFileName(publisherId, journalUuid);
		File file = new File(filePath);
		if (file.exists()) {
			boolean deleted = file.delete();
			if (!deleted) {
				log.error("File " + filePath + " cannot be deleted");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.crossover.trial.journals.service.FileService#getFile(com.crossover.
	 * trial.journals.model.Journal)
	 */
	@Override
	public byte[] getFile(Journal journal) throws IOException {
		File file = new File(getFileName(journal.getPublisher().getId(), journal.getUuid()));
		InputStream in = new FileInputStream(file);
		byte[] byteArray = IOUtils.toByteArray(in);
		in.close();
		return byteArray;
	}

}
