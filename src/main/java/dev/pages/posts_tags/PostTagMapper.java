package dev.pages.posts_tags;

import dev.pages.posts_tags.dto.PostTagCreateRequest;
import dev.pages.posts_tags.dto.PostTagUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostTagMapper {
    PostTagMapper INSTANCE = Mappers.getMapper(PostTagMapper.class);

    //@Mapping(source = "numberOfSeats", target = "seatCount")
    //@Mapping(target = "id", ignore = true)
    PostTag postTagFromPostTagCreateRequest(PostTagCreateRequest dto);

    void update(@MappingTarget PostTag tag, PostTagUpdateRequest dto);
}
