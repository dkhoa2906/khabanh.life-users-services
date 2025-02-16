package life.khabanh.usersservices.repository;

import life.khabanh.usersservices.entity.GeneratedImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GeneratedImageRepository extends JpaRepository<GeneratedImage, Long> {
    boolean existsById(String id);
    List<GeneratedImage> findByUserId(String userId);
    GeneratedImage findById(String id);
}
