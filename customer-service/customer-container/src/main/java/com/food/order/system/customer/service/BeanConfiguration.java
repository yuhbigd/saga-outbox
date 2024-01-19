package com.food.order.system.customer.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.food.order.system.customer.domain.CustomerDomainService;
import com.food.order.system.customer.domain.CustomerDomainServiceImpl;

@Configuration
public class BeanConfiguration {
    @Bean
    public CustomerDomainService customerDomainService() {
        return new CustomerDomainServiceImpl();
    }
}
