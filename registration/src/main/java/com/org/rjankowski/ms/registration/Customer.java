package com.org.rjankowski.ms.registration;

import lombok.Data;

@Data
public class Customer {
    private Long id;
    private String firstName;
    private String lastName;
    private Boolean active;
}
