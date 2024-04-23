CREATE TABLE users
(
    id          INT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(255)                                            NOT NULL,
    email       VARCHAR(255)                                            NOT NULL UNIQUE KEY,
    password    VARCHAR(255)                                            NOT NULL,
    role        ENUM ('ADMIN', 'DOCTOR', 'PATIENT') DEFAULT 'PATIENT'   NOT NULL,
    dob         DATE                                                    NULL,
    gender      ENUM ('MALE', 'FEMALE', 'OTHER')    DEFAULT 'MALE'      NOT NULL,
    phone       VARCHAR(20)                                             NULL,
    picture     VARCHAR(255)                                            NULL,
    blood_group ENUM ('A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-') NULL,
    address     JSON                                                    NULL,
    created_at  TIMESTAMP                           DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP                           DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);