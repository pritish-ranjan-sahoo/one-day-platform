package com.oneday.entity;

import com.oneday.entity.type.OttPlatformType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OttProvider {
    @Id
    private Long providerId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "provider_id")
    private AppUser user;

    @Enumerated(EnumType.STRING)
    private OttPlatformType providerName;

    @Column(nullable = false)
    private Double commissionPct;

    private String paymentDestination;

    @OneToMany(mappedBy = "provider", cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private Set<ServicePack> packs = new HashSet<>();
}
