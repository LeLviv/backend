package ua.garage.lelviv.complaints.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ua.garage.lelviv.complaints.dto.ComplaintRequest;
import ua.garage.lelviv.complaints.dto.ComplaintResponse;
import ua.garage.lelviv.complaints.entities.Complaint;
import ua.garage.lelviv.complaints.entities.ComplaintRepository;
import ua.garage.lelviv.complaints.exception.ComplaintFindException;

@Service
@RequiredArgsConstructor
public class ComplaintsService {
    private final ComplaintRepository repository;
    private final FileUploadService fileUploadService;

    public ComplaintResponse processComplaint(ComplaintRequest complaintRequest, String username) {
        String photoUrl = null;
        MultipartFile photo = complaintRequest.getPhoto();

        if (photo != null && !photo.isEmpty()) {
            photoUrl = fileUploadService.saveSafeFile(photo);
        }

        try {

            var problem = Complaint.builder()
                    .problem(complaintRequest.getComplaint())
                    .longitude(complaintRequest.getLongitude())
                    .latitude(complaintRequest.getLatitude())
                    .author(username)
                    .photoPath(photoUrl)
                    .build();

            repository.save(problem);
            return new ComplaintResponse("Скаргу успішно подано");
        } catch (Exception e) {
            fileUploadService.deleteFile(photoUrl);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public Page<Complaint> getComplaints(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return repository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Complaint getComplaint(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new ComplaintFindException("Скаргу з ID " + id + " не знайдено"));
    }
}
