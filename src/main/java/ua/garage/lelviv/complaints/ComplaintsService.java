package ua.garage.lelviv.complaints;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ua.garage.lelviv.authorization.entities.Role;
import ua.garage.lelviv.authorization.entities.UserRepository;

@Service
@RequiredArgsConstructor
public class ComplaintsService {
    private final ComplaintRepository repository;

    private static final String UPLOAD_DIR = "uploads/complaints/";
    private final UserRepository userRepository;

    public ComplaintResponse processComplaint(ComplaintRequest complaintRequest, String username) {
        String photoUrl = null;
        MultipartFile photo = complaintRequest.getPhoto();

        if (photo != null && !photo.isEmpty()) {
            try {
                String originalFileName = photo.getOriginalFilename();
                String extension = "";
                if (originalFileName != null && originalFileName.contains(".")) {
                    extension = originalFileName.substring(originalFileName.lastIndexOf("."));
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

    public List<Complaint> getComplaints(String username) {
        var user = userRepository
                .findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Користувача не знайдено"));

        var userRole = user.getRole();

        if (userRole != Role.ADMIN) {
            throw new AccessDeniedException("Тільки адміни мають право переглядати скарги");
        }

        return repository.findAll();
    }

    public Complaint getComplaint(String username, Long id) {
        var user = userRepository
                .findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Користувача не знайдено"));

        var userRole = user.getRole();

        if (userRole != Role.ADMIN) {
            throw new AccessDeniedException("Тільки адміни мають право переглядати скарги");
        }

        return repository
                .findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Скаргу з ID " + id + " не знайдено"));
    }
}
