ALTER TABLE orders DROP COLUMN orders_group_id;
ALTER TABLE orders ADD COLUMN orders_group_id INT REFERENCES orders_group ON DELETE SET NULL