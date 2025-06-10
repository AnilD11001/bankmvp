package bank.mvp.repository;

import bank.mvp.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    boolean existsByPanAndType(String pan, String type);
}
