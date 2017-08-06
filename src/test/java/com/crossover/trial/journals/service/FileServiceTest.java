package com.crossover.trial.journals.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.multipart.MultipartFile;

import com.crossover.trial.journals.Application;
import com.crossover.trial.journals.service.exception.DirectoryCannotCreatedException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FileServiceTest {
	
	@Autowired
	private FileService fileService;

	@Autowired
	private DirectoryService directoryService;

	private long id = 1333;
	private String uuid = UUID.randomUUID().toString();
	
	@Test
	public void uploadDeleteFile() throws IOException, DirectoryCannotCreatedException{
		File dir = new File(directoryService.getDirectoryName(id));
		directoryService.createDirectory(dir);
		
		File file = new File("src/test/resources/TestFile.pdf");
	    FileInputStream input = new FileInputStream(file);
	    MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "application/pdf", 
	    		IOUtils.toByteArray(input));
	    input.close();
	    
	    fileService.uploadFile(multipartFile, id, uuid);
	    
		File f = new File(fileService.getFileName(id, uuid));
		assertTrue(f.exists());
		
		fileService.deleteFile(id, uuid);
		assertFalse(f.exists());
	}
	
}
