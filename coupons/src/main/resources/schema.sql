CREATE TABLE COUPONS (
  id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  CUSTOMER_ID BIGINT NOT NULL,
  BARCODE VARCHAR(255) NOT NULL,
  STATUS VARCHAR(50) NOT NULL
);