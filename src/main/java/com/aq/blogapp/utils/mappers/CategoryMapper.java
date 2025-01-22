package com.aq.blogapp.utils.mappers;

import com.aq.blogapp.entity.Category;
import com.aq.blogapp.vo.dto.CategoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    //    @Mapping(target = "blogs", source = "")
    Category categoryDtoToCategory(CategoryDto categoryDTO);

    CategoryDto categoryToCategoryDto(Category category);
}
