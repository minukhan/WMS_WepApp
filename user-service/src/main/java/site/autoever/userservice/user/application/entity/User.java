package site.autoever.userservice.user.application.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String address;
    private String name;
    @Column(name = "phone_number")
    private String phoneNumber;
    private String role;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
