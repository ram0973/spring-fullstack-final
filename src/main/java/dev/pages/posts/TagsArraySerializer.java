package dev.pages.posts;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import dev.pages.posts_tags.PostTag;

import java.io.IOException;
import java.util.Collection;

public class TagsArraySerializer extends JsonSerializer<Collection<? extends PostTag>> {

    @Override
    public void serialize(Collection<? extends PostTag> tags, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartArray();
        for (PostTag tag : tags) {
            jsonGenerator.writeString(tag.getTitle());
        }
        jsonGenerator.writeEndArray();
    }
}
