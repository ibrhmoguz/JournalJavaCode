package com.crossover.trial.journals.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crossover.trial.journals.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
