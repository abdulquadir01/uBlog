package dev.aq.ublog.services.impl;

import dev.aq.ublog.entities.Category;
import dev.aq.ublog.exceptions.ResourceNotFoundException;
import dev.aq.ublog.respositories.CategoryRepository;
import dev.aq.ublog.services.CategoryService;
import dev.aq.ublog.utils.AppUtils;
import dev.aq.ublog.utils.mappers.CategoryMapper;
import dev.aq.ublog.vo.dto.CategoryDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

import static dev.aq.ublog.constants.UBlogConstants.CATEGORY_ID;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
  private final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

  private final CategoryMapper categoryMapper;
  private final CategoryRepository categoryRepository;

  @Override
  public List<CategoryDto> getAllCategory() {

    return categoryRepository.findAll()
        .stream()
        .map(categoryMapper::categoryToCategoryDto)
        .toList();
  }


  @Override
  public CategoryDto getCategoryById(Long id) {
    CategoryDto categoryDtoById = new CategoryDto();

    try {
      if (id != null) {
        categoryDtoById = categoryRepository.findById(id)
            .map(categoryMapper::categoryToCategoryDto)
            .orElseThrow(() -> new ResourceNotFoundException("Category", CATEGORY_ID, id));
      }
    } catch (NoSuchElementException ex) {
      throw new ResourceNotFoundException("Category", CATEGORY_ID, id);
    }

    return categoryDtoById;
  }


  @Override
  public CategoryDto createCategory(CategoryDto categoryDTO) {
    CategoryDto newCategoryDto = new CategoryDto();

    try {
      if (Boolean.FALSE.equals(AppUtils.anyEmpty(categoryDTO))) {
        newCategoryDto = saveAndReturnDTO(categoryMapper.categoryDtoToCategory(categoryDTO));
      }
    } catch (Exception ex) {
      logger.info("Error message: {}", ex.getMessage());
      logger.info("Error cause : {}", String.valueOf(ex.getCause()));
    }

    return newCategoryDto;
  }


  @Override
  public CategoryDto updateCategory(Long id, CategoryDto categoryDTO) {
    CategoryDto updatedCategory = new CategoryDto();
    Category existingCategory;

    try {
      existingCategory = categoryRepository.findById(id)
          .orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", id));

      existingCategory.setCategoryTitle(categoryDTO.getCategoryTitle());
      existingCategory.setCategoryDescription(categoryDTO.getCategoryDescription());

      updatedCategory = categoryMapper.categoryToCategoryDto(categoryRepository.save(existingCategory));

    } catch (ResourceNotFoundException rnfe) {
      logger.info(rnfe.getMessage());
    }

    return updatedCategory;
  }


  @Override
  public void deleteCategory(Long id) {

    try {
      categoryRepository.deleteById(id);
    } catch (EmptyResultDataAccessException ERDAE) {
      System.out.println(ERDAE.getMessage());
      System.out.println(ERDAE.getCause());
      throw new ResourceNotFoundException("Category", "CategoryId", id);
    }

  }


  //  PRIVATE methods
  private CategoryDto saveAndReturnDTO(Category category) {
    CategoryDto returnedDto = new CategoryDto();

    if (category != null) {
      Category savedCategory = categoryRepository.save(category);
      returnedDto = categoryMapper.categoryToCategoryDto(savedCategory);
    }

    return returnedDto;
  }

}
