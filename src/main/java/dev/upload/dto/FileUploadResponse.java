package dev.upload.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

@Data
public class FileUploadResponse {
    // {"status":"success","data":{"url":"https://tmpfiles.org/11914163/1687551.jfif"}}
    @NonNull String status;
    @NonNull Data data;

    public FileUploadResponse(@NonNull String status, @NonNull Data data) {
        this.status = status;
        this.data = data;
    }

    @Getter
    @Setter
    public static class Data {
        @NonNull String url;
        public Data(@NonNull String url) {
            this.url = url;
        }
    }
}




