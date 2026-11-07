package ua.garage.lelviv.userInfo;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.garage.lelviv.authorization.entities.User;
import ua.garage.lelviv.authorization.entities.UserRepository;

@Service
@RequiredArgsConstructor
public class UserInfoService {
    private final UserRepository repository;

    public String getUsername(String email) {
        User user = repository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Такого користувача не знайдено"));

        return user.getVisibleName();
    }
}
