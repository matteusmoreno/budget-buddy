CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL ,
    price DECIMAL(19, 2) NOT NULL,
    stock_quantity INTEGER NOT NULL,
    image VARCHAR(255) NOT NULL ,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    active BOOLEAN
);
