CREATE TABLE transactions(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sender varchar(13) NOT NULL,
    receiver varchar(13) NOT NULL ,
    amount DECIMAL(19,4) CONSTRAINT amount_positive CHECK (amount > 0) NOT NULL ,
    currency        CHAR(3) NOT NULL DEFAULT 'RON',
    status VARCHAR(20) NOT NULL,
    transfer_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (sender) REFERENCES accounts(account_number),
    FOREIGN KEY (receiver) REFERENCES accounts(account_number)
) ENGINE=InnoDB;