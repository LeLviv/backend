package ua.garage.lelviv.storage;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ua.garage.lelviv.complaints.exception.FileStorageException;
import ua.garage.lelviv.complaints.exception.InvalidFileTypeException;

@Component
@RequiredArgsConstructor
public class FileUploadValidator {
    private final Tika tika;

    public ValidatedFile validateAndPrepare(MultipartFile file, String folder, List<String> allowedMimeTypes) {
        if (file == null || file.isEmpty()) {
            throw new FileStorageException("Cannot upload an empty file.");
        }

        String sanitizedFolder = sanitizeFolder(folder);

        try {
            String detectedType;
            try (InputStream inputStream = file.getInputStream()) {
                detectedType = tika.detect(inputStream);
            }

            if (!allowedMimeTypes.contains(detectedType)) {
                throw new InvalidFileTypeException("Invalid file format: " + detectedType);
            }

            String filename = UUID.randomUUID() + getExtensionFromMimeType(detectedType);
            String storageKey = sanitizedFolder.isEmpty() ? filename : sanitizedFolder + "/" + filename;

            return new ValidatedFile(storageKey, detectedType);

        } catch (IOException e) {
            throw new FileStorageException("Failed to read uploaded file: " + e.getMessage());
        }
    }

    private String sanitizeFolder(String folder) {
        if (folder == null || folder.isBlank()) {
            return "";
        }

        String normalized = folder.trim().replace('\\', '/');

        int start = 0;
        int end = normalized.length();
        while (start < end && normalized.charAt(start) == '/') {
            start++;
        }
        while (end > start && normalized.charAt(end - 1) == '/') {
            end--;
        }

        String cleaned = normalized.substring(start, end);
        if (cleaned.isEmpty()) {
            return "";
        }

        for (String segment : cleaned.split("/")) {
            if (segment.isEmpty() || segment.equals(".") || segment.equals("..")) {
                throw new FileStorageException("Invalid folder path: " + folder);
            }
        }

        return cleaned;
    }

    private String getExtensionFromMimeType(String mimeType) {
        return switch (mimeType) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            case "application/pdf" -> ".pdf";
            default -> "";
        };
    }

    public record ValidatedFile(String storageKey, String contentType) {}
}
