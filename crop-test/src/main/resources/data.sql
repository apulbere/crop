INSERT INTO pet_category(id, code) VALUES (1, 'animal');
INSERT INTO pet_category(id, code) VALUES (2, 'plant');

INSERT INTO pet_type(id, code, pet_category_id) VALUES (1, 'dog', 1);
INSERT INTO pet_type(id, code, pet_category_id) VALUES (2, 'cat', 1);
INSERT INTO pet_type(id, code, pet_category_id) VALUES (3, 'unknown', 1);

INSERT INTO pet(id, birthdate, name, price, pet_type_id, active) VALUES (1, '2000-10-01', 'Britney', 55.6, 1, false);
INSERT INTO pet(id, birthdate, name, price, pet_type_id, active) VALUES (2, '2002-12-01', 'Brownie', 8.2, 1, false);
INSERT INTO pet(id, birthdate, name, price, pet_type_id, active) VALUES (3, '2010-01-11', 'Bailey', 1.9, 2, true);
INSERT INTO pet(id, birthdate, name, price, pet_type_id, active) VALUES (4, '2010-01-11', 'Bailey4', 1.1, 3, false);
INSERT INTO pet(id, birthdate, name, price, pet_type_id, active) VALUES (5, '2010-01-11', 'Bailey5', 1.1, 3, false);
INSERT INTO pet(id, birthdate, name, price, pet_type_id, active) VALUES (6, '2010-01-11', 'Bailey6', 1.1, 3, false);
INSERT INTO pet(id, birthdate, name, price, pet_type_id, active) VALUES (7, '2010-01-11', 'Bailey7', 1.1, 3, false);
INSERT INTO pet(id, birthdate, name, price, pet_type_id, active) VALUES (8, '2010-01-11', 'Bailey8', 1.1, 3, false);
INSERT INTO pet(id, birthdate, name, price, pet_type_id, active) VALUES (9, '2010-01-11', 'Bailey9', 1.1, 3, false);
INSERT INTO pet(id, birthdate, name, price, pet_type_id, active) VALUES (10, '2010-01-11', 'Bailey10', 1.1, 3, false);

INSERT INTO pet_feature(id, feature, pet_id) VALUES (1, 'fast', 1);
INSERT INTO pet_feature(id, feature, pet_id) VALUES (2, 'cute', 2);