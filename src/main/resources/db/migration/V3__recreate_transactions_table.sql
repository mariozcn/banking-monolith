DROP TABLE IF EXISTS transactions;

CREATE TABLE transactions(
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             sender BIGINT NOT NULL,
                             receiver BIGINT NOT NULL ,
                             amount DECIMAL(19,4) CONSTRAINT amount_positive CHECK (amount > 0) NOT NULL ,
                             currency        CHAR(3) NOT NULL DEFAULT 'RON',
                             status VARCHAR(20) NOT NULL,
                             transfer_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                             FOREIGN KEY (sender) REFERENCES accounts(id),
                             FOREIGN KEY (receiver) REFERENCES accounts(id)
) ENGINE=InnoDB;