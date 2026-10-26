package ua.garage.lelviv.complaints;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ComplaintsService {
    private final ComplaintRepository repository;

    private static final String UPLOAD_DIR = "uploads/complaints/";

    public ComplaintResponse processComplaint(ComplaintRequest complaintRequest, String username) {
        String photoUrl = null;
        MultipartFile photo = complaintRequest.getPhoto();

        if (photo != null && !photo.isEmpty()) {
            try {
                String fileName = UUID.randomUUID().toString() + "_" + photo.getOriginalFilename();
                Path filepath = Paths.get(UPLOAD_DIR + fileName);

                Files.createDirectories(filepath.getParent());
                Files.copy(photo.getInputStream(), filepath);

                photoUrl = "/complaints/photos/" + fileName;
            } catch (IOException e) {
                throw new RuntimeException("помилка збереження фото", e);
            }
        }

        var problem = Complaint.builder()
                .problem(complaintRequest.getComplaint())
                .longitude(complaintRequest.getLongitude())
                .latitude(complaintRequest.getLatitude())
                .author(username)
                .photoUrl(photoUrl)
                .build();

        repository.save(problem);
        return new ComplaintResponse("Скаргу успішно подано");
    }
}
