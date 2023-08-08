package com.sanil.electronic.store.services;

import com.sanil.electronic.store.dtos.CategoryDto;
import com.sanil.electronic.store.dtos.PageableResponse;
import com.sanil.electronic.store.entities.Category;

import java.util.List;

public interface CategoryService {

    //create
    CategoryDto create(CategoryDto categoryDto);

    //update
    CategoryDto update(CategoryDto categoryDto, String categoryId);

    //delete
    void delete(String categoryId);

    //getAll
    PageableResponse<CategoryDto> getAll(int pageNumber, int pageSize, String sort, String sortDir);

    //get single category detail
    CategoryDto getById(String categoryId);

    //search
    List<CategoryDto> searchCategory(String keyword);

}
