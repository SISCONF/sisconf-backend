ALTER TABLE stock_food DROP CONSTRAINT stock_food_pk;
ALTER TABLE stock_food ADD COLUMN id SERIAL;
ALTER TABLE stock_food ADD CONSTRAINT stock_food_pk PRIMARY KEY (id);