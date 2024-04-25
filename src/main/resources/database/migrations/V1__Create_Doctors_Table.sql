CREATE TABLE doctors
(
    id               INT PRIMARY KEY AUTO_INCREMENT,
    user_id          INT          NOT NULL,
    bio              tinytext     NULL,
    spacialities     VARCHAR(255) NOT NULL,
    fees             INT          NOT NULL,
    education        VARCHAR(255) NULL,
    schedule         VARCHAR(255) NULL,
    experience       VARCHAR(255) NULL,
    designation      VARCHAR(255) NOT NULL,
    hospital         VARCHAR(255) NOT NULL,
    hospital_address VARCHAR(255) NOT NULL,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id)
);