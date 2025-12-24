package dev.aq.ublog.utils.mappers;

import dev.aq.ublog.entities.Category;
import dev.aq.ublog.vo.dto.CategoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryMapper {

  CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

  //    @Mapping(target = "blogs", source = "")
  Category categoryDtoToCategory(CategoryDto categoryDTO);

  CategoryDto categoryToCategoryDto(Category category);
}
