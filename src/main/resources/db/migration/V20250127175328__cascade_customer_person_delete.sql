ALTER TABLE customer DROP COLUMN person_id;
ALTER TABLE customer ADD COLUMN person_id INT NOT NULL REFERENCES person ON DELETE CASCADE;