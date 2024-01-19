package com.food.order.system.customer.dataaccess.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.food.order.system.customer.dataaccess.entity.CustomerEntity;
import java.util.UUID;

public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, UUID> {
}
