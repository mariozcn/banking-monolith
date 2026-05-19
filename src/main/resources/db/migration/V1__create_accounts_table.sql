CREATE TABLE accounts (
                          id              BIGINT AUTO_INCREMENT PRIMARY KEY,
                          account_number  VARCHAR(20) NOT NULL UNIQUE,
                          owner_name      VARCHAR(100) NOT NULL,
                          balance         DECIMAL(19, 4) NOT NULL DEFAULT 0,
                          currency        CHAR(3) NOT NULL DEFAULT 'RON',
                          created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;