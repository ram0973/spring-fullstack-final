package ra.web.page.users;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import ra.web.page.roles.UserRole;

import java.io.IOException;
import java.util.Collection;

public class RolesArraySerializer extends JsonSerializer<Collection<? extends UserRole>> {

    @Override
    public void serialize(Collection<? extends UserRole> roles, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartArray();
        for (UserRole role : roles) {
            jsonGenerator.writeString(role.getRole().toString());
        }
        jsonGenerator.writeEndArray();
    }
}
