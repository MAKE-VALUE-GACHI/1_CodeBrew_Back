package codebrew.doctorgeondam.repository.jpa;

import codebrew.doctorgeondam.entity.ChatLogEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatJpaRepository extends JpaRepository<ChatLogEntity, Long> {
  List<ChatLogEntity> findByUser_IdOrderByTimestampDesc(Long userId);
}

