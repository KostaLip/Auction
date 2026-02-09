INSERT INTO bank_account_model (id, email, usd_amount, eur_amount, rsd_amount)
SELECT nextval('bank_account_seq'), 'user@gmail.com', 0, 0, 0
WHERE NOT EXISTS (
    SELECT 1 FROM bank_account_model WHERE email = 'user@gmail.com'
);