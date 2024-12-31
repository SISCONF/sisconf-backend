CREATE TABLE IF NOT EXISTS stock_food(
    stock_id INT NOT NULL REFERENCES stock ON DELETE CASCADE,
    food_id INT NOT NULL REFERENCES food ON DELETE CASCADE,
    quantity INT NOT NULL DEFAULT 1,
    CONSTRAINT stock_food_pk PRIMARY KEY (stock_id, food_id)
);