INSERT INTO user_model (id, email, password, name, role)
SELECT nextval('user_seq'), 'admin@gmail.com', '$2a$10$kyN5Ex6KPiQ/opRqFpdCceXSLbt4990VRuThIh5IodBpJhNvqgczu', 'Djordje', 'ADMIN'
WHERE NOT EXISTS (
    SELECT 1 FROM user_model WHERE email = 'admin@gmail.com'
);

INSERT INTO user_model (id, email, password, name, role)
SELECT nextval('user_seq'), 'user@gmail.com', '$2a$10$dYNTlyq9qRYh35hWNdIBduWXrfLcewzpDb1BhKCg0u41AE0t7lWnW', 'Kosta', 'USER'
WHERE NOT EXISTS (
    SELECT 1 FROM user_model WHERE email = 'user@gmail.com'
);