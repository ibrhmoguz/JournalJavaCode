package com.crossover.trial.journals.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crossover.trial.journals.model.Category;
import com.crossover.trial.journals.repository.CategoryRepository;
import com.crossover.trial.journals.service.exception.CategoryNotFoundException;

/**
 * The Class CategoryServiceImpl.
 */
@Service
public class CategoryServiceImpl implements CategoryService{

	/** The category repository. */
	private final CategoryRepository categoryRepository;
	
	/**
	 * Instantiates a new category service impl.
	 *
	 * @param categoryRepository the category repository
	 */
	@Autowired
	public CategoryServiceImpl(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	/* (non-Javadoc)
	 * @see com.crossover.trial.journals.service.CategoryService#findAll()
	 */
	@Override
	public List<Category> findAll() {
		return categoryRepository.findAll();
	}

	/* (non-Javadoc)
	 * @see com.crossover.trial.journals.service.CategoryService#findByCategoryId(java.lang.Long)
	 */
	@Override
	public Category findByCategoryId(Long categoryId) throws CategoryNotFoundException {
		Category category = categoryRepository.findOne(categoryId);
		if (category == null) {
			throw new CategoryNotFoundException("Category with Id " + categoryId + " not found.");
		}
		return category;
	}

}
