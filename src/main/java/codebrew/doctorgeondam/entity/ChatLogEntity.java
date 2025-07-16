package codebrew.doctorgeondam.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatLogEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id") // FK 이름
  private UserEntity user;

  @Column(length = 1000)
  private String userMessage;

  @Column(length = 2000)
  private String aiReply;

  private LocalDateTime timestamp;

}

