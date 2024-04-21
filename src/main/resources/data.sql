-- Insert payment methods
--INSERT INTO payment_methods (method_name) VALUES ('CREDIT_CARD'), ('DEBIT_CARD'), ('PAYPAL'), ('BANK_TRANSFER'), ('IDEAL'), ('BITCOIN');

-- password = Admin01!
INSERT INTO users (username, password, email, first_name, last_name, date_of_birth, address, phone_no, user_created_on)
VALUES ('admin', '$2a$10$31SCjwJJfe.wMgkkErHSLuoeUtRJU82VVmLj7YxvQTTKhU3oacqMG', 'admin@test.nl', 'Tony', 'Stark', '1980-01-01', 'Stark Street 1', '0987654321', '2024-04-18');

-- password = Klaartjee!
INSERT INTO users (username, password, email, first_name, last_name, date_of_birth, address, phone_no, user_created_on)
VALUES ('klaartjee', '$2a$10$ptQDYLbM0avPG.Da3.J3WuSWlhmUqCtORq.gnXkJchq/xL36z9.ce', 'klaartje.koeman@example.com', 'Klaartje', 'Koeman', '1990-01-01', '123 Main Street', '123-456-7890', '2022-04-20');

-- password = SpiderMan01
INSERT INTO users (username, password, email, first_name, last_name, date_of_birth, address, phone_no, user_created_on)
VALUES ('spiderman', '$2a$10$j7o2mOoQi.AKtGAUZB8TXuQVSzwb5DUKGmIv18aoL1OR8aYTgHwnC', 'spider.man@example.com', 'Peter', 'Parker', '1998-09-21', '33 Web Street', '133-321-1323', '2022-04-20');

INSERT INTO authorities (username, authority) VALUES ('klaartjee', 'ROLE_USER');
INSERT INTO authorities (username, authority) VALUES ('spiderman', 'ROLE_USER');
INSERT INTO authorities (username, authority) VALUES ('spiderman', 'ROLE_DESIGNER');
INSERT INTO authorities (username, authority) VALUES ('admin', 'ROLE_USER');
INSERT INTO authorities (username, authority) VALUES ('admin', 'ROLE_ADMIN');