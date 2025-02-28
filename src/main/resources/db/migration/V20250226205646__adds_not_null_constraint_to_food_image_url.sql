UPDATE food SET image_url = 'https://sisconf-foods-images-bucket.s3.us-east-2.amazonaws.com/food-placeholder.jpg' WHERE image_url IS NULL;
ALTER TABLE food ALTER COLUMN image_url SET NOT NULL;
