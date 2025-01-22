package com.aq.blogapp.controllers;

import com.aq.blogapp.exceptions.ResourceNotFoundException;
import com.aq.blogapp.services.CategoryService;
import com.aq.blogapp.utils.AppUtils;
import com.aq.blogapp.vo.dto.CategoryDto;
import com.aq.blogapp.vo.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

  private final Logger logger = LoggerFactory.getLogger(CategoryController.class);

  private final CategoryService categoryService;

  @GetMapping
  public ResponseEntity<Object> getAllCategory() {
    List<CategoryDto> categoryDtoList = new ArrayList<>();

    try {
      categoryDtoList = categoryService.getAllCategory();

      return new ResponseEntity<>(categoryDtoList, HttpStatus.OK);

    } catch (Exception ex) {

      return new ResponseEntity<>(
        new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
          HttpStatus.INTERNAL_SERVER_ERROR.value()
        ),
        HttpStatus.INTERNAL_SERVER_ERROR
      );
    }

  }


  @GetMapping("/{categoryId}")
  public ResponseEntity<Object> getCategoryById(@PathVariable Long categoryId) {
    CategoryDto categoryDtoById;

    try {
      categoryDtoById = categoryService.getCategoryById(categoryId);
      logger.info("CategoryDto by id : {}", categoryDtoById);

      return new ResponseEntity<>(categoryDtoById, HttpStatus.OK);

    } catch (ResourceNotFoundException ex) {

      return new ResponseEntity<>(
        new ApiResponse(HttpStatus.NOT_FOUND.getReasonPhrase(),
          HttpStatus.NOT_FOUND.value()
        ),
        HttpStatus.NOT_FOUND
      );
    }

  }


  @PostMapping
  public ResponseEntity<Object> createCategory(@Valid @RequestBody CategoryDto categoryDTO) {
    CategoryDto createdCategoryDto;

    try {
      if (Boolean.FALSE.equals(AppUtils.anyEmpty(categoryDTO))) {
        createdCategoryDto = categoryService.createCategory(categoryDTO);

        return new ResponseEntity<>(createdCategoryDto, HttpStatus.CREATED);
      } else {

        return new ResponseEntity<>(
          new ApiResponse(HttpStatus.BAD_REQUEST.getReasonPhrase(),
            HttpStatus.BAD_REQUEST.value()
          ),
          HttpStatus.BAD_REQUEST
        );
      }

    } catch (Exception ex) {

      return new ResponseEntity<>(
        new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
          HttpStatus.INTERNAL_SERVER_ERROR.value()
        ),
        HttpStatus.INTERNAL_SERVER_ERROR
      );
    }
  }


  @PutMapping("/{categoryId}")
  public ResponseEntity<Object> updateCategory(@PathVariable Long categoryId, @Valid @RequestBody CategoryDto categoryDTO) {
    CategoryDto updatedCategory;

    try {
      updatedCategory = categoryService.updateCategory(categoryId, categoryDTO);

      return new ResponseEntity<>(updatedCategory, HttpStatus.OK);

    } catch (Exception ex) {

      return new ResponseEntity<>(
        new ApiResponse("Internal Server Error",
          HttpStatus.INTERNAL_SERVER_ERROR.value()
        ),
        HttpStatus.INTERNAL_SERVER_ERROR
      );
    }

  }


  @DeleteMapping("/{categoryId}")
  public ResponseEntity<Object> deleteCategory(@PathVariable Long categoryId) {

    try {
      categoryService.deleteCategory(categoryId);

      return new ResponseEntity<>(
        new ApiResponse("Category Deleted Successfully",
          HttpStatus.OK.value()
        ),
        HttpStatus.OK
      );

    } catch (Exception ex) {

      return new ResponseEntity<>(
        new ApiResponse(
          HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
          HttpStatus.INTERNAL_SERVER_ERROR.value()
        ),
        HttpStatus.INTERNAL_SERVER_ERROR
      );
    }
  }

}
