package com.org.rjankowski.ms.coupons;

import lombok.Data;

@Data
public class Customer {
    private Long id;
    private String firstName;
    private String lastName;
    private Boolean active;
}