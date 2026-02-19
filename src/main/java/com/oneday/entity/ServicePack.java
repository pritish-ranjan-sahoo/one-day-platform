package com.oneday.entity;

import com.oneday.entity.type.PackType;
import com.oneday.entity.type.ServiceStatusType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_pack_from_platforms",
                        columnNames = { "provider","price","packType" }
                )
        }
)
public class ServicePack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long packId;

    @ManyToOne
    @JoinColumn(name = "provided_by")
    private OttProvider provider;

    private int validityInDays;

    @Column(nullable = false)
    private double price;

    @Enumerated(EnumType.STRING)
    private PackType packType;

    private String featureDesc;

    @Enumerated(EnumType.STRING)
    private ServiceStatusType packStatus;

}
