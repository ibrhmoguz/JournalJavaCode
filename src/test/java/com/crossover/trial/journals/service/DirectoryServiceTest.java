package com.crossover.trial.journals.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.crossover.trial.journals.Application;
import com.crossover.trial.journals.service.exception.DirectoryCannotCreatedException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DirectoryServiceTest {
	
	@Autowired
	private DirectoryService directoryService;

	private final static long id = 12;
	
	@Test
	public void getDirectoryName(){
		assertTrue(directoryService.getDirectoryName(id).contains("/" + id));
	}

	@Test
	public void createDirectory_GivenValidPath_ShouldCreate() throws DirectoryCannotCreatedException{
		File dir = new File("C:\\1.Dev\\_CO\\upload\\13121212");
		directoryService.createDirectory(dir);
		assertTrue(dir.exists());
	}

	@Test
	public void createDirectory_GivenInvalidPath_ShouldThrowException() throws DirectoryCannotCreatedException{
		File dir = new File("Z:\\NotValidPath");
		assertThatThrownBy(() -> directoryService.createDirectory(dir)).isInstanceOf(DirectoryCannotCreatedException.class);
	}
	
}
