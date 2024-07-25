CREATE TABLE customers (
    id BINARY(16) PRIMARY KEY NOT NULL,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(255) NOT NULL UNIQUE,
    cpf VARCHAR(255) NOT NULL UNIQUE,
    address_id BIGINT,
    country VARCHAR(50) NOT NULL,
    balance DECIMAL(10, 2),
    role ENUM('ADMIN', 'CUSTOMER') NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    active TINYINT NOT NULL,

    FOREIGN KEY (address_id) REFERENCES addresses(id)
);
