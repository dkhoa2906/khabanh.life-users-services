package life.khabanh.usersservices.repository;

import life.khabanh.usersservices.entity.InviteCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InviteCodeRepository extends JpaRepository<InviteCode, Long> {
    boolean existsByCode(String code);
    InviteCode findByCode(String code);
}
