package dev.upload;

import dev.pages.MultiPartFileUtils;
import dev.upload.dto.FileUploadRequest;
import dev.upload.dto.FileUploadResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.TimeZone;


@Service
public class FileUploadService {
    FileUploadResponse upload(@Valid FileUploadRequest dto) {
        return saveMultiPartImage(dto.file());
    }

    private static Path getResourceAsFile(String relativeFilePath) throws FileNotFoundException {
        return ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + relativeFilePath).toPath();
    }

    public static FileUploadResponse saveMultiPartImage(MultipartFile image) {
        String imagePath = "";
        try {
            Path root = getResourceAsFile("static/upload/images");
            String originalFileName = image.getOriginalFilename();
            assert originalFileName != null;
            String originalFileExtension = originalFileName.substring(originalFileName.lastIndexOf('.') + 1);
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            String year = String.valueOf(cal.get(Calendar.YEAR));
            String month = String.valueOf(cal.get(Calendar.MONTH) + 1);
            String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));

            Path pathWithDate = Files.createDirectories(Path.of(root.toString(), year, month, day));
            Path newFilePath = Files.createTempFile(pathWithDate, "", "." + originalFileExtension);
            image.transferTo(newFilePath);

            // for Windows OS
            imagePath = newFilePath.toString().replace("\\", "/");
            if (imagePath.contains("/upload")) {
                imagePath = imagePath.substring(imagePath.indexOf("/upload"));
            }
        } catch (IOException ignored) {
            return new FileUploadResponse("error", new FileUploadResponse.Data(imagePath));
        }
        return new FileUploadResponse("success", new FileUploadResponse.Data(imagePath));
    }
}
