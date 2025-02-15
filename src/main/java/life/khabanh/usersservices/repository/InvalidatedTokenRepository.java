package life.khabanh.usersservices.repository;

import life.khabanh.usersservices.entity.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {
    boolean existsById(String token);
}
