CREATE TABLE IF NOT EXISTS food(
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    category VARCHAR(9),
    unit_price DECIMAL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);