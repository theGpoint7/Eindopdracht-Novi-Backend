-- Insert roles
INSERT INTO roles (rolename) VALUES ('ROLE_USER'), ('ROLE_ADMIN'), ('ROLE_DESIGNER');

-- Insert payment methods
INSERT INTO payment_methods (method_name) VALUES ('CREDIT_CARD'), ('DEBIT_CARD'), ('PAYPAL'), ('BANK_TRANSFER'), ('IDEAL'), ('BITCOIN');

-- Insert users
INSERT INTO users (user_id) VALUES (1);

-- Insert user credentials
INSERT INTO user_credentials (user_id, username, password_hash)
VALUES (1, 'JohnnyBravo', '$2a$12$Bv5eNzEIPfKenyMd3CdI5uh0gq/T32QxBh4D520Su/671IPU8XBRq');

-- Insert user profile
INSERT INTO user_profile (user_id, first_name, last_name, email, address, phone_no)
VALUES (1, 'John', 'Paardman', 'johnpaardman@example.com', 'hooischuur 12 2345AS', '0612345678');

-- Insert user roles
INSERT INTO user_roles (user_id, rolename) VALUES (1, 'ROLE_ADMIN'), (1, 'ROLE_USER'), (1, 'ROLE_DESIGNER');

-- postman didnt want to add a user with id 1 because it's given to the admin, so let's start at 3
ALTER SEQUENCE users_user_id_seq RESTART WITH 2;
