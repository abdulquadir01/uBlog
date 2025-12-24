package dev.aq.ublog.utils.mappers;

import dev.aq.ublog.entities.Blog;
import dev.aq.ublog.vo.dto.BlogDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BlogMapper {

  BlogMapper INSTANCE = Mappers.getMapper(BlogMapper.class);

  Blog blogDtoToBlog(BlogDto blogDTO);

  BlogDto blogToBlogDto(Blog blog);
}
