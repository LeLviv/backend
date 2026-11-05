package ua.garage.lelviv.complaints;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ComplaintsService {
    private final ComplaintRepository repository;

    private static final String UPLOAD_DIR = "uploads/complaints/";

    public ComplaintResponse processComplaint(ComplaintRequest complaintRequest, String username) {
        String photoUrl = null;
        MultipartFile photo = complaintRequest.getPhoto();

        if (photo != null && !photo.isEmpty()) {
            String contentType = photo.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new IllegalArgumentException("Дозволено завантажувати лише зображення");
            }

            try {
                String originalFilename = photo.getOriginalFilename();

                String extension = "";
                if (StringUtils.hasText(originalFilename)) {
                    String ext = org.springframework.util.StringUtils.getFilenameExtension(originalFilename);
                    extension = ext != null ? "." + ext : "";
                }

                String fileName = UUID.randomUUID() + extension;
                Path filepath = Paths.get(UPLOAD_DIR + fileName);

                Files.createDirectories(filepath.getParent());
                Files.copy(photo.getInputStream(), filepath);

                photoUrl = "/complaints/photos/" + fileName;
            } catch (IOException e) {
                throw new PhotoSavingException("Помилка збереження фото");
            }
        }

        var problem = Complaint.builder()
                .problem(complaintRequest.getComplaint())
                .longitude(complaintRequest.getLongitude())
                .latitude(complaintRequest.getLatitude())
                .author(username)
                .photoPath(photoUrl)
                .build();

        repository.save(problem);
        return new ComplaintResponse("Скаргу успішно подано");
    }

    public Page<Complaint> getComplaints(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return repository.findAll(pageable);
    }

    public Complaint getComplaint(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new ComplaintFindException("Скаргу з ID " + id + " не знайдено"));
    }
}

