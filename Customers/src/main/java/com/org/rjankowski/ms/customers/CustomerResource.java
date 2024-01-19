package com.org.rjankowski.ms.customers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController("/")
@RequiredArgsConstructor
public class CustomerResource {
    private static Long idSequence = 11L;

    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;

    @Autowired
    private Environment environment;

    @GetMapping("/customers")
    public ResponseEntity<List> getCustomers(@RequestParam(value = "firstName", required = false) String firstName, @RequestParam(value = "lastName", required = false) String lastName) {
        if (firstName != null && lastName != null) {
            return new ResponseEntity(customerRepository.findAllByFirstNameAndLastName(firstName, lastName), HttpStatus.OK);
        }
        if (firstName != null) {
            return new ResponseEntity(customerRepository.findAllByFirstName(firstName), HttpStatus.OK);
        }
        if (lastName != null) {
            return new ResponseEntity(customerRepository.findAllByLastName(lastName), HttpStatus.OK);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-port", environment.getProperty("server.port", "1234"));
        return new ResponseEntity(customerRepository.findAll(), headers, HttpStatus.OK);
    }

    @GetMapping("customers/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable Long id) {
        Optional<Customer> itemById = customerRepository.findById(id);
        if (itemById.isPresent()) {
            return new ResponseEntity<>(itemById.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
    }

    @PostMapping("/customers")
    public ResponseEntity createCustomer(@RequestBody Customer customer) {
        customer.setId(idSequence++);
        customerRepository.saveAndFlush(customer);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/customers")
    public ResponseEntity modifyCustomer(@RequestBody Customer customer) {
        customerRepository.saveAndFlush(customer);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("customers/{id}")
    public ResponseEntity deleteCustomer(@PathVariable Long id) {
        customerRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("addresses")
    public ResponseEntity<List<Address>> findAllByCustomerId(@RequestParam(value = "customerId", required = true) Long customerId) {
        List<Address> addresses = addressRepository.findAllByCustomerId(customerId);

        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }

    @GetMapping("addresses/{id}")
    public ResponseEntity<Address> findById(@PathVariable Long id) {
        Optional<Address> address = addressRepository.findById(id);

        return address.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
