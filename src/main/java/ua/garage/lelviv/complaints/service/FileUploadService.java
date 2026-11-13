package ua.garage.lelviv.complaints.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.garage.lelviv.complaints.exception.FileStorageException;
import ua.garage.lelviv.complaints.exception.InvalidFileTypeException;

@Service
public class FileUploadService {

    private final Tika tika = new Tika();
    private static final List<String> ALLOWED_MIME_TYPES = List.of("image/jpeg", "image/png", "image/webp");

    private static final String UPLOAD_DIR = "uploads/complaints/";

    public String saveSafeFile(MultipartFile file) {
        try {
            String detectedType;
            try (InputStream inputStream = file.getInputStream()) {
                detectedType = tika.detect(inputStream);
            }

            if (!ALLOWED_MIME_TYPES.contains(detectedType)) {
                throw new InvalidFileTypeException("Invalid File format. only images allowed.");
            }

            String extension = getExtensionFromMimeType(detectedType);
            String safeFilename = UUID.randomUUID() + extension;

            Path uploadPath = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(safeFilename);

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }

            return UPLOAD_DIR + safeFilename;

        } catch (IOException e) {
            throw new FileStorageException("error during file saving: " + e.getMessage());
        }
    }

    public void deleteFile(String filePath) {
        if (filePath != null) {
            try {
                Files.deleteIfExists(Paths.get(filePath));
            } catch (IOException ignored) {
            }
        }
    }

    private String getExtensionFromMimeType(String mimeType) {
        return switch (mimeType) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            default -> "";
        };
    }
}
