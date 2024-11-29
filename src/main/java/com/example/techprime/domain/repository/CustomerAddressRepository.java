package com.example.techprime.domain.repository;

import com.example.techprime.domain.entities.Customer;
import com.example.techprime.domain.entities.CustomerAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, Integer> {
    List<CustomerAddress> findByCustomer(Customer customer);
    Optional<CustomerAddress> findById(int id);
}
