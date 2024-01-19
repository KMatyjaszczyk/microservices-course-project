package com.org.rjankowski.ms.customers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//change to interface with extends JpaRepository
// add new required correct methods
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {


    List<Customer> findAllByFirstNameAndLastName(String firstName, String lastName);


    List<Customer> findAllByFirstName(String firstName);

    List<Customer> findAllByLastName(String lastName);


    Optional<Customer> findById(Long id);

//    public void saveAndFlush(Customer customer) {
//        throw new RuntimeException();
//
//    }
//
//    public void deleteById(Long id) {
//        throw new RuntimeException();
//    }
}

