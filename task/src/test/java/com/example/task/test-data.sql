CREATE TABLE account (
    ac_no VARCHAR(255) NOT NULL,
    ac_owner VARCHAR(255),
    balance DOUBLE,
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    UNIQUE (ac_no)
);
INSERT INTO account (ac_no, ac_owner, balance) VALUES ('234523435', 'John Doe', 10000.0);
INSERT INTO account (ac_no, ac_owner, balance) VALUES ('123456789', 'Jane Smith', 5000.0);
INSERT INTO account (ac_no, ac_owner, balance) VALUES ('456789123', 'Alice Johnson', 3000.0);