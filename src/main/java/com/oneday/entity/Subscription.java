package com.oneday.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subscriptionId;

    @ManyToOne
    @JoinColumn(name = "buyer_details")
    private Customer buyer;

    @ManyToOne
    @JoinColumn(name = "pack_details")
    private ServicePack packDetails;

    @Column(nullable = false,updatable = false)
    private LocalDateTime rechargeDate;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    @OneToOne
    @JoinColumn(name = "payment_details")
    private Transaction paymentDetails;

}
