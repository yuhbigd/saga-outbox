package com.food.order.system.customer.dataaccess.adapter;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.food.order.system.customer.dataaccess.mapper.CustomerDataAccessMapper;
import com.food.order.system.customer.dataaccess.repository.CustomerJpaRepository;
import com.food.order.system.customer.domain.entity.Customer;
import com.food.order.system.customer.service.ports.output.repository.CustomerRepository;
import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class CustomerRepositoryImpl implements CustomerRepository {
    private final CustomerJpaRepository repository;
    private final CustomerDataAccessMapper customerDataAccessMapper;

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Customer createCustomer(Customer customer) {
        return customerDataAccessMapper.customerEntityToCustomer(
                repository.save(customerDataAccessMapper.customerToCustomerEntity(customer)));
    }


}
