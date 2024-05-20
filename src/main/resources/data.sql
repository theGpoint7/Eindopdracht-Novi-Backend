-- Voeg gebruikers toe
INSERT INTO users (username, password, email, first_name, last_name, date_of_birth, address, phone_no, user_created_on, enabled_account)
VALUES ('admin', '$2a$10$31SCjwJJfe.wMgkkErHSLuoeUtRJU82VVmLj7YxvQTTKhU3oacqMG', 'admin@test.nl', 'Tony', 'Stark', '1980-01-01', 'Starkstart 1 Amsterdam', '06-12121212', '2024-04-18', true);

INSERT INTO users (username, password, email, first_name, last_name, date_of_birth, address, phone_no, user_created_on, enabled_account)
VALUES ('klaartjee', '$2a$10$ptQDYLbM0avPG.Da3.J3WuSWlhmUqCtORq.gnXkJchq/xL36z9.ce', 'klaartje.koeman@example.com', 'Klaartje', 'Koeman', '1990-01-01', 'zijstraat 12 Utrecht', '06-13121213', '2022-04-20', true);

INSERT INTO users (username, password, email, first_name, last_name, date_of_birth, address, phone_no, user_created_on, enabled_account)
VALUES ('spiderman', '$2a$10$j7o2mOoQi.AKtGAUZB8TXuQVSzwb5DUKGmIv18aoL1OR8aYTgHwnC', 'spider.man@example.com', 'Peter', 'Parker', '1998-09-21', '33 kevlarstraat Biddinghuizen', '06-14121214', '2022-04-20', true);

INSERT INTO users (username, password, email, first_name, last_name, date_of_birth, address, phone_no, user_created_on, enabled_account)
VALUES ('petergriffin', '$2a$04$bzYtNiq8sFxb4reQ0vWKIetj/KKdECMxIqB39pwBf3b9jpHw14x2y', 'fashionista@example.com', 'Alice', 'de Groot', '1995-05-05', 'parkwegpad 7 Zwolle', '06-15121215', '2024-04-20', true);

-- Voeg autoriteiten toe
INSERT INTO authorities (username, authority) VALUES ('admin', 'ROLE_CUSTOMER');
INSERT INTO authorities (username, authority) VALUES ('admin', 'ROLE_ADMIN');

INSERT INTO authorities (username, authority) VALUES ('klaartjee', 'ROLE_CUSTOMER');

INSERT INTO authorities (username, authority) VALUES ('spiderman', 'ROLE_CUSTOMER');
INSERT INTO authorities (username, authority) VALUES ('spiderman', 'ROLE_DESIGNER');

INSERT INTO authorities (username, authority) VALUES ('petergriffin', 'ROLE_CUSTOMER');
INSERT INTO authorities (username, authority) VALUES ('petergriffin', 'ROLE_DESIGNER');

-- Voeg ontwerpers toe
INSERT INTO designers (username, store_name, bio)
VALUES ('spiderman', 'Spiderman kledingwinkel', 'Ik verkoop mijn spiderman pak!');

INSERT INTO designers (username, store_name, bio)
VALUES ('petergriffin', 'Peter Griffin''s Boutique', 'Goede mode voor mooie mensen');

-- Voeg een product toe aan de AbstractProduct tabel (met de juiste naam)
INSERT INTO abstract_product (product_name, product_type, material, price, inventory_count, image_url, product_description, designer_id)
VALUES ('Klassieke Sneakers', 'Footwear', 'Kunststof', 59.99, 150, 'http://example.com/images/classic-sneakers.jpg', 'Klassieke sneakers geschikt voor dagelijks gebruik.',
        (SELECT designer_id FROM designers WHERE username = 'spiderman'));

-- Voeg specifiek veld toe voor Footwear
INSERT INTO footwear (product_id, footwear_size, gender)
VALUES ((SELECT product_id FROM abstract_product WHERE product_name = 'Klassieke Sneakers'), 42, 'Unisex');

-- Voeg een promotie toe
INSERT INTO promotions (promotion_name, promotion_description, promotion_percentage, promotion_start_date_time, promotion_end_date_time, designer_id)
VALUES ('Lentebries uitverkoop', 'Alle laatste lente ontwerpen nu met extra 20% korting', 20, '2024-05-10T00:00:00', '2024-07-20T23:59:59',
        (SELECT designer_id FROM designers WHERE username = 'spiderman'));

-- Koppel de promotie aan het product
UPDATE abstract_product
SET promotion_id = (SELECT promotion_id FROM promotions WHERE promotion_name = 'Lentebries uitverkoop')
WHERE product_name = 'Klassieke Sneakers';

-- Voeg feedback toe voor het product
INSERT INTO feedback (username, product_id, content, feedback_date_time)
VALUES ('klaartjee', (SELECT product_id FROM abstract_product WHERE product_name = 'Klassieke Sneakers'),
        'Leuk product, ik heb er nog 4 extra gekocht.', CURRENT_TIMESTAMP);

-- Voeg feedback toe voor de ontwerper
INSERT INTO feedback (username, designer_id, content, feedback_date_time)
VALUES ('klaartjee', (SELECT designer_id FROM designers WHERE username = 'spiderman'),
        'Echt een toffe designer', CURRENT_TIMESTAMP);
