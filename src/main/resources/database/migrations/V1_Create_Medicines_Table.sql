CREATE  TABLE  medicines (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    prescriptions_id INT NOT NULL,
    name VARCHAR(100),
    morning BOOLEAN NOT NULL ,
    afternoon BOOLEAN NOT NULL ,
    night BOOLEAN NOT NULL ,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (prescriptions_id) REFERENCES prescriptions(id)
);