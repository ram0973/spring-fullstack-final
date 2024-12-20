package ra.web.common.util;

import lombok.experimental.UtilityClass;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.TimeZone;

@UtilityClass
public class MultiPartFileUtils {

    private static Path getResourceAsFile(String relativeFilePath) throws FileNotFoundException {
        return ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + relativeFilePath).toPath();
    }

    // TODO: move to application properties
    public static String saveMultiPartImage(MultipartFile image) throws IOException {
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
        String imagePath = newFilePath.toString().replace("\\", "/");
        if (imagePath.contains("/upload")) {
            imagePath = imagePath.substring(imagePath.indexOf("/upload"));
        }
        return imagePath;
    }
}
