INSERT INTO category (name) VALUES ('Part');
INSERT INTO category (name) VALUES ('Kitchen');
INSERT INTO category (name) VALUES ('Work');
INSERT INTO category (name) VALUES ('Living Room');
INSERT INTO category (name) VALUES ('Bedroom');
INSERT INTO category (name) VALUES ('Outdoor');
INSERT INTO category (name) VALUES ('Bathroom');
INSERT INTO category (name) VALUES ('Garden');
INSERT INTO category (name) VALUES ('Entertainment');
INSERT INTO category (name) VALUES ('Furniture');


INSERT INTO item (name, description, quantity, is_product) VALUES ('Wooden Chair Leg', 'One leg of a chair', 20, false);
INSERT INTO item (name, description, quantity, is_product) VALUES ('Wooden Chair Seat', 'The seat of a chair', 20, false);
INSERT INTO item (name, description, quantity, is_product) VALUES ('Wooden Chair Back', 'The back of a chair', 20, false);
INSERT INTO item (name, description, quantity, is_product) VALUES ('Wooden Chair Armrest', 'One armrest of a chair', 20, false);

INSERT INTO item (name, description, quantity, is_product) VALUES ('Steel Chair Leg', 'One leg of a chair', 20, false);
INSERT INTO item (name, description, quantity, is_product) VALUES ('Steel Chair Seat', 'The seat of a chair', 20, false);
INSERT INTO item (name, description, quantity, is_product) VALUES ('Steel Chair Back', 'The back of a chair', 20, false);
INSERT INTO item (name, description, quantity, is_product) VALUES ('Steel Chair Armrest', 'One armrest of a chair', 20, false);

INSERT INTO item (name, description, quantity, is_product) VALUES ('Plastic Chair Leg', 'One leg of a chair', 20, false);
INSERT INTO item (name, description, quantity, is_product) VALUES ('Plastic Chair Seat', 'The seat of a chair', 20, false);
INSERT INTO item (name, description, quantity, is_product) VALUES ('Plastic Chair Back', 'The back of a chair', 20, false);
INSERT INTO item (name, description, quantity, is_product) VALUES ('Plastic Chair Armrest', 'One armrest of a chair', 20, false);

INSERT INTO item (name, description, quantity, is_product) VALUES ('Wooden Chair', 'Wooden chair', 20, true);
INSERT INTO item (name, description, quantity, is_product) VALUES ('Steel Chair', 'Steel chair', 20, true);
INSERT INTO item (name, description, quantity, is_product) VALUES ('Plastic Chair', 'Plastic chair', 20, true);


INSERT INTO item_category (item_id, category_id) VALUES (1,1);
INSERT INTO item_category (item_id, category_id) VALUES (2,1);
INSERT INTO item_category (item_id, category_id) VALUES (3,1);
INSERT INTO item_category (item_id, category_id) VALUES (4,1);
INSERT INTO item_category (item_id, category_id) VALUES (5,1);
INSERT INTO item_category (item_id, category_id) VALUES (6,1);
INSERT INTO item_category (item_id, category_id) VALUES (7,1);
INSERT INTO item_category (item_id, category_id) VALUES (8,1);
INSERT INTO item_category (item_id, category_id) VALUES (9,1);
INSERT INTO item_category (item_id, category_id) VALUES (10,1);
INSERT INTO item_category (item_id, category_id) VALUES (11,1);
INSERT INTO item_category (item_id, category_id) VALUES (12,1);
INSERT INTO item_category (item_id, category_id) VALUES (13,10);
INSERT INTO item_category (item_id, category_id) VALUES (13,2);
INSERT INTO item_category (item_id, category_id) VALUES (14,10);
INSERT INTO item_category (item_id, category_id) VALUES (14,4);
INSERT INTO item_category (item_id, category_id) VALUES (15,10);
INSERT INTO item_category (item_id, category_id) VALUES (15,6);


INSERT INTO bom (product_id, component_id, unit) VALUES (13,1,4);
INSERT INTO bom (product_id, component_id, unit) VALUES (13,2,1);
INSERT INTO bom (product_id, component_id, unit) VALUES (13,3,1);
INSERT INTO bom (product_id, component_id, unit) VALUES (13,4,2);

INSERT INTO bom (product_id, component_id, unit) VALUES (14,5,4);
INSERT INTO bom (product_id, component_id, unit) VALUES (14,6,1);
INSERT INTO bom (product_id, component_id, unit) VALUES (14,7,1);
INSERT INTO bom (product_id, component_id, unit) VALUES (14,8,2);

INSERT INTO bom (product_id, component_id, unit) VALUES (15,9,4);
INSERT INTO bom (product_id, component_id, unit) VALUES (15,10,1);
INSERT INTO bom (product_id, component_id, unit) VALUES (15,11,1);
INSERT INTO bom (product_id, component_id, unit) VALUES (15,12,2);


INSERT INTO customer (id, email) VALUES ('user_1','khang@gmail.com');

