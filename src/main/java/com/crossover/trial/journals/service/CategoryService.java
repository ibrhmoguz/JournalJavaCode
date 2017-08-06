package com.crossover.trial.journals.service;

import java.util.List;

import com.crossover.trial.journals.model.Category;
import com.crossover.trial.journals.service.exception.CategoryNotFoundException;

/**
 * The Interface CategoryService.
 */
public interface CategoryService {

	/**
	 * Find all.
	 *
	 * @return the list
	 */
	List<Category> findAll();

	/**
	 * Find by category id.
	 *
	 * @param categoryId the category id
	 * @return the category
	 * @throws CategoryNotFoundException the category not found exception
	 */
	Category findByCategoryId(Long categoryId) throws CategoryNotFoundException;

}
