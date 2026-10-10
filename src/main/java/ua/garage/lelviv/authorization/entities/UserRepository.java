package ua.garage.lelviv.authorization.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.garage.lelviv.authorization.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByPhoneNumber(String PhoneNumber);
}
