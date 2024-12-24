package dev.upload.dto;

import org.springframework.web.multipart.MultipartFile;

public record FileUploadRequest(MultipartFile file) {

}
