CREATE TABLE cards (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    number VARCHAR(255) NOT NULL,
    cvv VARCHAR(10) NOT NULL,
    expiration_date VARCHAR(10) NOT NULL,
    card_type ENUM('CREDIT', 'DEBIT') NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    active TINYINT NOT NULL
);
