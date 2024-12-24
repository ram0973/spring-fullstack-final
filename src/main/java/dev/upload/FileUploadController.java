package dev.upload;

import dev.upload.dto.FileUploadRequest;
import dev.upload.dto.FileUploadResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/v1/upload")
public class FileUploadController {
    @NonNull
    private final FileUploadService fileUploadService;

    @PostMapping(path = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileUploadResponse> upload(@Valid @ModelAttribute FileUploadRequest dto) {
        FileUploadResponse fileUploadResponse = fileUploadService.upload(dto);
        log.info("File {} uploaded", fileUploadResponse.toString());
        return new ResponseEntity<>(fileUploadResponse, HttpStatus.CREATED);
    }
}
