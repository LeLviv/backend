package ua.garage.lelviv.storage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.garage.lelviv.complaints.exception.FileStorageException;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.storage.type", havingValue = "local", matchIfMissing = true)
public class LocalFileUploadService implements FileUploadService {
    private final FileUploadValidator validator;

    @Value("${app.storage.local.base-dir:uploads}")
    private String baseDir;

    @Value("${app.storage.local.public-prefix:/uploads}")
    private String publicPrefix;

    @Override
    public String saveSafeFile(MultipartFile file, String folder, List<String> allowedMimeTypes) {
        FileUploadValidator.ValidatedFile validated = validator.validateAndPrepare(file, folder, allowedMimeTypes);

        try {
            Path rootPath = Paths.get(baseDir).toAbsolutePath().normalize();
            Path targetPath = rootPath.resolve(validated.storageKey()).normalize();

            if (!targetPath.startsWith(rootPath)) {
                throw new FileStorageException("Blocked file write outside upload directory.");
            }

            Files.createDirectories(targetPath.getParent());

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
            }

            return validated.storageKey();

        } catch (IOException e) {
            throw new FileStorageException("Error saving file locally: " + e.getMessage());
        }
    }

    @Override
    public void deleteFile(String storageKey) {
        if (storageKey == null || storageKey.isBlank()) {
            return;
        }

        try {
            Path rootPath = Paths.get(baseDir).toAbsolutePath().normalize();
            Path targetPath = rootPath.resolve(storageKey).normalize();

            if (!targetPath.startsWith(rootPath) || targetPath.equals(rootPath)) {
                log.warn("Blocked attempt to delete file outside upload directory: {}", storageKey);
                return;
            }

            Files.deleteIfExists(targetPath);
        } catch (IOException | InvalidPathException e) {
            log.warn("Failed to delete local file: {}", storageKey, e);
        }
    }

    @Override
    public String getFileUrl(String storageKey) {
        if (storageKey == null || storageKey.isBlank()) {
            return null;
        }
        String prefix =
                publicPrefix.endsWith("/") ? publicPrefix.substring(0, publicPrefix.length() - 1) : publicPrefix;
        return prefix + "/" + storageKey;
    }
}
