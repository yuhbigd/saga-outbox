package com.food.order.system.data.access.customer.entity;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@DynamicUpdate
@Table(name = "customers")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerEntity {

    @Id
    private UUID id;

    private String username;
    private String firstName;
    private String lastName;
}
