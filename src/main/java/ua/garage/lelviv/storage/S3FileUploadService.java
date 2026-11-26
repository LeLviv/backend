package ua.garage.lelviv.storage;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import ua.garage.lelviv.complaints.exception.FileStorageException;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.storage.type", havingValue = "s3")
public class S3FileUploadService implements FileUploadService {

    private final FileUploadValidator validator;
    private final S3Client s3Client;

    @Value("${app.storage.s3.bucket}")
    private String bucketName;

    @Value("${app.storage.s3.public-url-prefix}")
    private String publicUrlPrefix;

    @Override
    public String saveSafeFile(MultipartFile file, String folder, List<String> allowedMimeTypes) {
        FileUploadValidator.ValidatedFile validated = validator.validateAndPrepare(file, folder, allowedMimeTypes);

        try {
            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(validated.storageKey())
                    .contentType(validated.contentType())
                    .build();

            try (InputStream inputStream = file.getInputStream()) {
                s3Client.putObject(putRequest, RequestBody.fromInputStream(inputStream, file.getSize()));
            }

            return validated.storageKey();

        } catch (IOException | S3Exception e) {
            throw new FileStorageException("Error uploading file to S3: " + e.getMessage());
        }
    }

    @Override
    public void deleteFile(String storageKey) {
        if (storageKey == null || storageKey.isBlank()) {
            return;
        }

        try {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(storageKey)
                    .build());
        } catch (S3Exception e) {
            log.warn("Failed to delete file from S3: {}", storageKey, e);
        }
    }

    @Override
    public String getFileUrl(String storageKey) {
        if (storageKey == null || storageKey.isBlank()) {
            return null;
        }
        String prefix = publicUrlPrefix.endsWith("/")
                ? publicUrlPrefix.substring(0, publicUrlPrefix.length() - 1)
                : publicUrlPrefix;
        return prefix + "/" + storageKey;
    }
}
