package com.sanil.electronic.store.services.impl;

import com.sanil.electronic.store.dtos.CategoryDto;
import com.sanil.electronic.store.dtos.PageableResponse;
import com.sanil.electronic.store.dtos.UserDto;
import com.sanil.electronic.store.entities.Category;
import com.sanil.electronic.store.entities.User;
import com.sanil.electronic.store.exception.ResourceNotFoundException;
import com.sanil.electronic.store.helper.Helper;
import com.sanil.electronic.store.repositories.CategoryRepository;
import com.sanil.electronic.store.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper mapper;
    Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
    @Value("${category.profile.image.path}")
    private String imagePath;

    @Override
    public CategoryDto create(CategoryDto categoryDto) {

        //creating categoryId randomly
        String categoryId = UUID.randomUUID().toString();
        categoryDto.setCategoryId(categoryId);

        Category category = mapper.map(categoryDto, Category.class);
        Category savedCategory = categoryRepository.save(category);
        return mapper.map(savedCategory, CategoryDto.class);
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto, String categoryId) {

        //get category of given id
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with Given id "));

        //update category details
        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setImageName(categoryDto.getImageName());
        Category udatedCategory = categoryRepository.save(category);
        return mapper.map(udatedCategory, CategoryDto.class);
    }

    @Override
    public void delete(String categoryId) throws ResourceNotFoundException {
        //get category details
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with given Id "));

        //delete category profile image
        String fullPath = imagePath + category.getImageName();
        try {
            Path path = Paths.get(fullPath);
            Files.delete(path);
        } catch (IOException e) {
            logger.info("User image not found in folder");

        }
        categoryRepository.delete(category);
    }

    @Override
    public PageableResponse<CategoryDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Category> page = categoryRepository.findAll(pageable);
        PageableResponse<CategoryDto> response = Helper.getPageableResponse(page, CategoryDto.class);
        return response;
    }

    @Override
    public CategoryDto getById(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with the given id "));
        return mapper.map(category, CategoryDto.class);
    }

    @Override
    public List<CategoryDto> searchCategory(String keyword) {
        List<Category> categories = categoryRepository.findByTitleContaining(keyword);
        List<CategoryDto> categoryDtos = categories.stream().map(category -> entityToDto(category)).collect(Collectors.toList());
        return categoryDtos;
    }

    private CategoryDto entityToDto(Category category) {
        return mapper.map(category, CategoryDto.class);
    }

    private Category dtoToEntity(CategoryDto categoryDto) {
        return mapper.map(categoryDto, Category.class);
    }
}