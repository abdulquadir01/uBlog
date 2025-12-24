package dev.aq.ublog.utils.mappers;

import dev.aq.ublog.entities.Comment;
import dev.aq.ublog.vo.dto.CommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CommentMapper {

  CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

  CommentDto commentToCommentDto(Comment comment);

  Comment commentDtoToComment(CommentDto commentDTO);

}
