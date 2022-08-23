CREATE TABLE IF NOT EXISTS test_db.Users (
    id int NOT NULL,
    login varchar(255),
    password varchar(255),
    PRIMARY KEY (id),
    UNIQUE (login)
    );

INSERT INTO test_db.Users VALUES (1, 'John', '1234');
INSERT INTO test_db.Users VALUES (2, 'Smith', '1234');


CREATE TABLE IF NOT EXISTS test_db.Logger (
    messageid int NOT NULL AUTO_INCREMENT,
    user_name varchar(255),
    message varchar(255),
    PRIMARY KEY (messageid),
    FOREIGN KEY (user_name) REFERENCES Users(login)
    );

CREATE TABLE IF NOT EXISTS test_db.Tokens (
    token varchar(255),
    user_id int NOT NULL,
    PRIMARY KEY (user_id),
    FOREIGN KEY (user_id) REFERENCES Users(id)
    );

