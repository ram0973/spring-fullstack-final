package ra.web.common.exceptions.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Getter
public class ValidationExceptionResponseDto {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private final ZonedDateTime timestamp = ZonedDateTime.now(ZoneId.of("UTC"));

    private final int status;
    private final String desc;
    private final String message = "Validation errors";
    private final String path;

    @JsonProperty("validationErrors")
    List<FieldViolation> fieldViolations;

    public ValidationExceptionResponseDto(String path, HttpStatusCode status, List<FieldViolation> fieldViolations) {
        this.path = path;
        this.status = status.value();
        this.desc = status.toString();
        this.fieldViolations = fieldViolations;
    }
}
