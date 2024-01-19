package com.org.rjankowski.ms.coupons;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class CouponsResource {
    private final CustomerApiClient customerApiClient;
    private final CouponRepository couponRepository;

    @PostMapping("/issue")
    public ResponseEntity issue(@RequestBody IssueCouponRequest request) {
        ResponseEntity<List<Customer>> response = customerApiClient.getCustomers();
        List<Customer> customers = response.getBody();

        boolean customerDoesNotExist = customers.stream().noneMatch(customer -> customer.getId().equals(request.getCustomerId()));

        if (customerDoesNotExist) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        Coupon coupon = new Coupon();
        coupon.setCustomerId(request.getCustomerId());
        coupon.setBarcode(UUID.randomUUID().toString());
        coupon.setStatus("ISSUED");

        couponRepository.saveAndFlush(coupon);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/use")
    public ResponseEntity<Void> use(@RequestBody UseCouponRequest request) {
        Optional<Coupon> couponOptional = couponRepository.findById(request.getCouponId());

        if (couponOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Coupon coupon = couponOptional.get();

        ResponseEntity<List<Customer>> response = customerApiClient.getCustomers();
        List<Customer> customers = response.getBody();

        boolean customerDoesNotExist = customers.stream().noneMatch(customer -> coupon.getCustomerId().equals(customer.getId()));
        if (customerDoesNotExist) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        coupon.setStatus("USED");
        couponRepository.saveAndFlush(coupon);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
