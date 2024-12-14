package com.catalog.cars.model.auth;

import com.catalog.cars.model.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "refresh_token")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private LocalDateTime expiryDate;

    private Boolean revoked;
}
