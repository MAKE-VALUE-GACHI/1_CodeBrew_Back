package codebrew.doctorgeondam.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.OneToOne;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String phoneNumber;

    private String email;
    private String password;
    private String gender;
    private String address;
    private String addressDetail;
    private LocalDate birthDate;
    private String job;
    private Double height;
    private Double weight;
    private Double bmi;
    
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.USER;

    @Column(name = "terms_agreed", nullable = false)
    @Builder.Default
    private Boolean termsAgreed = false;

    @Column(name = "privacy_policy_agreed", nullable = false)
    @Builder.Default
    private Boolean privacyPolicyAgreed = false;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserHealthProfileEntity healthProfile;
    
    public enum Role {
        USER, ADMIN
    }
}
