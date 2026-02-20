package com.oneday.entity;


import com.oneday.entity.type.AccountStatusType;
import com.oneday.entity.type.OAuthProviderType;
import com.oneday.entity.type.RoleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_provider_providerId",
                        columnNames = {"providerName","providerId"}
                )
        }
)
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long UserId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private OAuthProviderType providerName;

    private String providerId;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<RoleType> roles = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private AccountStatusType accountStatus;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
