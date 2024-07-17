CREATE TABLE customer_cards (
    customer_id BINARY(16) NOT NULL,
    card_id BIGINT NOT NULL,

    PRIMARY KEY (customer_id, card_id),
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    FOREIGN KEY (card_id) REFERENCES cards(id)
);
