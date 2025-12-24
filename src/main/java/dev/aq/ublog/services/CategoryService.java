package dev.aq.ublog.services;

import dev.aq.ublog.vo.dto.CategoryDto;

import java.util.List;


public interface CategoryService {

  List<CategoryDto> getAllCategory();

  CategoryDto getCategoryById(Long id);

  CategoryDto createCategory(CategoryDto categoryDTO);

  CategoryDto updateCategory(Long id, CategoryDto categoryDTO);

  void deleteCategory(Long id);

}
