package com.oneday.entity;


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
public class Customer {
    @Id
    private Long custId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "customer_id")
    private AppUser user;

    private String custName;

    @OneToMany(mappedBy = "buyer")
    private Set<Subscription> subscriptions = new HashSet<>();



}
