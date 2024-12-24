package dev.pages.posts;

import dev.common.exceptions.NoSuchEntityException;
import dev.pages.posts.dto.PostCreateRequest;
import dev.pages.posts.dto.PostUpdateRequest;
import dev.pages.posts_categories.PostCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    //@Mapping(source = "numberOfSeats", target = "seatCount")
    //@Mapping(target = "category", ignore = true)
    //@Mapping(target = "tags", ignore = true)
    //@Mapping(target = "image", ignore = true)
    //@Mapping(target = "id", ignore = true)
    Post postFromPostRequest(PostCreateRequest dto);

    //@Mapping(target = "category", ignore = true)
    //@Mapping(target = "tags", ignore = true)
    //@Mapping(target = "image", ignore = true)
    //@Mapping(target = "id", ignore = true)
    void update(@MappingTarget Post post, PostUpdateRequest dto);
}
