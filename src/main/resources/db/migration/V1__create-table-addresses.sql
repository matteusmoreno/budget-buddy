CREATE TABLE addresses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    zipcode VARCHAR(30) NOT NULL,
    street VARCHAR(200) NOT NULL,
    number VARCHAR(30) NOT NULL,
    neighborhood VARCHAR(200) NOT NULL,
    city VARCHAR(200) NOT NULL,
    state VARCHAR(50) NOT NULL,
    country VARCHAR(150) NOT NULL
);
