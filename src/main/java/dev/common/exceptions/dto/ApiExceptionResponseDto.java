package dev.common.exceptions.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter
public class ApiExceptionResponseDto {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private final ZonedDateTime timestamp = ZonedDateTime.now(ZoneId.of("UTC"));

    private final int status;
    private final String desc;
    private final String message;
    private final String path;

    public ApiExceptionResponseDto(String path, String message, HttpStatusCode status) {
        this.path = path;
        this.message = message;
        this.status = status.value();
        this.desc = status.toString();
    }
}


