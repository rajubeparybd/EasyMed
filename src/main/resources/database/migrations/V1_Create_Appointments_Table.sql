CREATE TABLE appointments
(
    id               INT PRIMARY KEY AUTO_INCREMENT,
    patient_id       INT                                                              NOT NULL,
    doctor_id        INT                                                              NOT NULL,
    appointment_date DATE                                                             NULL,
    status           ENUM ('Scheduled', 'Cancelled', 'Completed') DEFAULT 'Scheduled' NOT NULL,
    reason           VARCHAR(255)                                                     NOT NULL,
    created_at       TIMESTAMP                                    DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP                                    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES users (id),
    FOREIGN KEY (doctor_id) REFERENCES doctors (user_id)
);