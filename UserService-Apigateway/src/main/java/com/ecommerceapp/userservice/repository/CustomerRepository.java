package com.ecommerceapp.userservice.repository;

import com.ecommerceapp.userservice.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Optional<Customer> findByEmail(String username);
}
