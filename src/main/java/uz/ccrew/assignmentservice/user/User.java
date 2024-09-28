package uz.ccrew.assignmentservice.user;

import uz.ccrew.assignmentservice.base.Auditable;

import lombok.*;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(name = "user_u1", columnNames = "login")})
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class User extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String login;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(nullable = false, columnDefinition = "timestamp default CURRENT_TIMESTAMP")
    private LocalDateTime credentialsModifiedDate;

    @Column(nullable = true)
    private String fullName;

    @Column(nullable = true)
    private String email;
}