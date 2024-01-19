package com.org.rjankowski.ms.registration;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@EnableScheduling
public class RegistrationResource {
    private final RestTemplate restTemplate;
    private final List<RegisterCustomerRequest> fallbackRegistrations = new ArrayList<>();

    @PostMapping("/register")
    @CircuitBreaker(name = "register", fallbackMethod = "fallbackRegistration")
    public ResponseEntity<Void> register(@RequestBody RegisterCustomerRequest request) {
        Customer[] customersArray = restTemplate.getForObject("http://Customers/customers", Customer[].class);
        List<Customer> customers = Arrays.asList(customersArray);

        boolean customerExists = customers.stream()
                .anyMatch(customer -> request.getFirstName().equals(customer.getFirstName())
                        && request.getLastName().equals(customer.getLastName()));
        if (customerExists) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Customer customer = new Customer();
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setActive(Boolean.FALSE);

        restTemplate.postForEntity("http://Customers/customers", customer, Void.class);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> fallbackRegistration(@RequestBody RegisterCustomerRequest request, Throwable e) {
        fallbackRegistrations.add(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Scheduled(cron = "0 * * * * *")
    public void processFallbackRegistrations() {
        System.out.println("Removing fallback registration");
        if (!fallbackRegistrations.isEmpty()) {
            RegisterCustomerRequest fallbackRegistration = fallbackRegistrations.get(0);
            register(fallbackRegistration);
            fallbackRegistrations.remove(fallbackRegistration);
        }
    }

    @PostMapping("/active")
    @CircuitBreaker(name = "active-process", fallbackMethod = "fallbackActive")
    public ResponseEntity<Void> active(@RequestBody RegisterCustomerRequest request) throws InterruptedException {
        Thread.sleep(1000);
        Customer[] customersArray = restTemplate.getForObject("http://Customers/customers", Customer[].class);
        List<Customer> customers = Arrays.asList(customersArray);

        Optional<Customer> customerOptional = customers.stream().filter(customer -> request.getFirstName().equals(customer.getFirstName())
                && request.getLastName().equals(customer.getLastName())).findFirst();
        if (customerOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Customer customer = customerOptional.get();

        if (customer.getActive()) {
            return new ResponseEntity<>(HttpStatus.OK);
        }

        customer.setActive(Boolean.TRUE);

        restTemplate.put("http://Customers/customers", customer);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<Void> fallbackActive(@RequestBody RegisterCustomerRequest request, Throwable e) {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/close")
    public ResponseEntity<Void> close(@RequestBody RegisterCustomerRequest request) {
        Customer[] customersArray = restTemplate.getForObject("http://Customers/customers", Customer[].class);
        List<Customer> customers = Arrays.asList(customersArray);

        Optional<Customer> customerOptional = customers.stream().filter(customer -> request.getFirstName().equals(customer.getFirstName())
                && request.getLastName().equals(customer.getLastName())).findFirst();
        if (customerOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Customer customer = customerOptional.get();

        if (!customer.getActive()) {
            return new ResponseEntity<>(HttpStatus.OK);
        }

        customer.setActive(Boolean.FALSE);

        restTemplate.put("http://Customers/customers", customer);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
