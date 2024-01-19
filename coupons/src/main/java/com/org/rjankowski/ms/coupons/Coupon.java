package com.org.rjankowski.ms.coupons;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "COUPONS")
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long customerId;
    private String barcode;
    private String status;
}
