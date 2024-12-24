package dev.pages.posts_comments;

import dev.pages.posts_comments.dto.PostCommentCreateRequest;
import dev.pages.posts_comments.dto.PostCommentUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostCommentMapper {
    PostCommentMapper INSTANCE = Mappers.getMapper(PostCommentMapper.class);

    //@Mapping(source = "numberOfSeats", target = "seatCount")
    //@Mapping(target = "id", ignore = true)
    PostComment postCommentFromPostCommentCreateRequest(PostCommentCreateRequest dto);

    void update(@MappingTarget PostComment comment, PostCommentUpdateRequest dto);
}
