INSERT INTO product_model (id, owner_email, name, description, created_at)
SELECT nextval('product_seq'), 'user@gmail.com', 'Sablja', 'Veoma ostra sablja', CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM product_model WHERE owner_email = 'user@gmail.com' AND name = 'Sablja'
);