package com.aq.blogapp.utils.mappers;

import com.aq.blogapp.entity.Blog;
import com.aq.blogapp.vo.dto.BlogDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BlogMapper {

    BlogMapper INSTANCE = Mappers.getMapper(BlogMapper.class);

    Blog blogDtoToBlog(BlogDto blogDTO);

    BlogDto blogToBlogDto(Blog blog);
}
