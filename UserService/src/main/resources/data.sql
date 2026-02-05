INSERT INTO user_model (id, email, password, name, role)
SELECT 1, 'admin@gmail.com', 'password', 'Djordje', 'ADMIN'
WHERE NOT EXISTS (
    SELECT 1 FROM user_model WHERE email = 'admin@gmail.com'
);

INSERT INTO user_model (id, email, password, name, role)
SELECT 2, 'user@gmail.com', 'password', 'Kosta', 'USER'
WHERE NOT EXISTS (
    SELECT 1 FROM user_model WHERE email = 'user@gmail.com'
);