package com.crossover.trial.journals.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crossover.trial.journals.model.Category;
import com.crossover.trial.journals.service.CategoryService;

@RestController
@RequestMapping("/public/rest/category")
public class CategoryRestService {

    @Autowired
    private CategoryService categoryService;

    @RequestMapping(value = "")
    public List<Category> getCategories() {
        return categoryService.findAll();
    }

}
