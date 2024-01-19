package com.org.rjankowski.ms.customers;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private Boolean active;

    @OneToMany
    @JoinColumn(name = "customerId")
    List<Address> addresses;
}

