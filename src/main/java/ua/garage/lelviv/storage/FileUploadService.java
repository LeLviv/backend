package ua.garage.lelviv.storage;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    List<String> ALLOWED_MIME_TYPES = List.of("image/jpeg", "image/png", "image/webp");

    default String saveSafeFile(MultipartFile file, String folder) {
        return saveSafeFile(file, folder, ALLOWED_MIME_TYPES);
    }

    String saveSafeFile(MultipartFile file, String folder, List<String> allowedMimeTypes);

    void deleteFile(String storageKey);

    String getFileUrl(String storageKey);
}
