-- password = Admin01!
INSERT INTO users (username, password, email, first_name, last_name, date_of_birth, address, phone_no, user_created_on, enabled_account)
VALUES ('admin', '$2a$10$31SCjwJJfe.wMgkkErHSLuoeUtRJU82VVmLj7YxvQTTKhU3oacqMG', 'admin@test.nl', 'Tony', 'Stark', '1980-01-01', 'Stark Street 1', '0987654321', '2024-04-18', true);

INSERT INTO users (username, password, email, first_name, last_name, date_of_birth, address, phone_no, user_created_on, enabled_account)
VALUES ('klaartjee', '$2a$10$ptQDYLbM0avPG.Da3.J3WuSWlhmUqCtORq.gnXkJchq/xL36z9.ce', 'klaartje.koeman@example.com', 'Klaartje', 'Koeman', '1990-01-01', '123 Main Street', '123-456-7890', '2022-04-20', true);

INSERT INTO users (username, password, email, first_name, last_name, date_of_birth, address, phone_no, user_created_on, enabled_account)
VALUES ('spiderman', '$2a$10$j7o2mOoQi.AKtGAUZB8TXuQVSzwb5DUKGmIv18aoL1OR8aYTgHwnC', 'spider.man@example.com', 'Peter', 'Parker', '1998-09-21', '33 Web Street', '133-321-1323', '2022-04-20', true);

INSERT INTO users (username, password, email, first_name, last_name, date_of_birth, address, phone_no, user_created_on, enabled_account)
VALUES ('petergriffin', '$2a$04$bzYtNiq8sFxb4reQ0vWKIetj/KKdECMxIqB39pwBf3b9jpHw14x2y', 'fashionista@example.com', 'Alice', 'Fashionista', '1995-05-05', '789 Fashion Avenue', '987-654-3210', '2024-04-20', true);

INSERT INTO authorities (username, authority) VALUES ('admin', 'ROLE_CUSTOMER');
INSERT INTO authorities (username, authority) VALUES ('admin', 'ROLE_ADMIN');

INSERT INTO authorities (username, authority) VALUES ('klaartjee', 'ROLE_CUSTOMER');

INSERT INTO authorities (username, authority) VALUES ('spiderman', 'ROLE_CUSTOMER');
INSERT INTO authorities (username, authority) VALUES ('spiderman', 'ROLE_DESIGNER');

INSERT INTO authorities (username, authority) VALUES ('petergriffin', 'ROLE_CUSTOMER');
INSERT INTO authorities (username, authority) VALUES ('petergriffin', 'ROLE_DESIGNER');

INSERT INTO designers (username, store_name, bio)
VALUES ('spiderman', 'Spiderman clothing store', 'I sell you my spiderman suit!');

INSERT INTO designers (username, store_name, bio)
VALUES ('petergriffin', 'petergriffin''s Boutique', 'good fashion for pretty people');
