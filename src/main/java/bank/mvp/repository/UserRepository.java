package bank.mvp.repository;

import bank.mvp.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    boolean existsByPanAndType(String pan, String type);
    Optional<AppUser> findByUsername(String username);

    Boolean existsByUsername(String username);
}
