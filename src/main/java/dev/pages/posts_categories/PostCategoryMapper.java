package dev.pages.posts_categories;

import dev.pages.posts_categories.dto.PostCategoryCreateRequest;
import dev.pages.posts_categories.dto.PostCategoryUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostCategoryMapper {
    PostCategoryMapper INSTANCE = Mappers.getMapper(PostCategoryMapper.class);

    //@Mapping(source = "numberOfSeats", target = "seatCount")
    //@Mapping(target = "id", ignore = true)
    PostCategory postCategoryFromPostCategoryRequest(PostCategoryCreateRequest dto);

    void update(@MappingTarget PostCategory category, PostCategoryUpdateRequest dto);
}
