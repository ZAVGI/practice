package com.example.finance.service;

import com.example.finance.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto create(CategoryDto dto);

    List<CategoryDto> findAll();

    CategoryDto findById(Long id);

    CategoryDto update(Long id, CategoryDto dto);

    void delete(Long id);
}
