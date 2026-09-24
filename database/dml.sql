-- DML - PRUEBA TÉCNICA


-- 1. INSERTAR UN CLIENTE
INSERT INTO clients (
    identification_type,
    identification_number,
    first_name,
    last_name,
    email,
    birth_date,
    created_at,
    updated_at
)
VALUES (
           'CC',
           '1234567890',
           'Sergio',
           'Prueba',
           'sergio.prueba@gmail.com',
           '2000-01-01',
           CURRENT_TIMESTAMP,
           CURRENT_TIMESTAMP
       );


-- 2. CONSULTAR CLIENTES
SELECT *
FROM clients;


-- 3. CONSULTAR UN CLIENTE POR SU IDENTIFICACIÓN
SELECT *
FROM clients
WHERE identification_type = 'CC'
  AND identification_number = '1234567890';


-- 4. ACTUALIZAR UN CLIENTE
UPDATE clients
SET first_name = 'Sergio Andres',
    updated_at = CURRENT_TIMESTAMP
WHERE identification_type = 'CC'
  AND identification_number = '1234567890';


-- 5. INSERTAR UNA CUENTA DE AHORROS
INSERT INTO accounts (
    account_type,
    account_number,
    status,
    balance,
    available_balance,
    gmf_exempt,
    created_at,
    updated_at,
    client_id
)
VALUES (
           'SAVINGS',
           '5312345678',
           'ACTIVE',
           0.00,
           0.00,
           FALSE,
           CURRENT_TIMESTAMP,
           CURRENT_TIMESTAMP,
           (
               SELECT id
               FROM clients
               WHERE identification_type = 'CC'
                 AND identification_number = '1234567890'
           )
       );


-- 6. CONSULTAR CUENTAS
SELECT *
FROM accounts;


-- 7. INSERTAR UN DEPÓSITO
INSERT INTO transactions (
    transaction_type,
    movement_type,
    amount,
    transaction_date,
    destination_account_id,
    transfer_id,
    account_id
)
VALUES (
           'DEPOSIT',
           'CREDIT',
           50000.00,
           CURRENT_TIMESTAMP,
           NULL,
           NULL,
           (
               SELECT id
               FROM accounts
               WHERE account_number = '5312345678'
           )
       );


-- 8. ACTUALIZAR SALDOS DESPUÉS DEL DEPÓSITO
UPDATE accounts
SET balance = balance + 50000.00,
    available_balance = available_balance + 50000.00,
    updated_at = CURRENT_TIMESTAMP
WHERE account_number = '5312345678';


-- 9. CONSULTAR MOVIMIENTOS DE UNA CUENTA
SELECT t.*
FROM transactions t
         JOIN accounts a
              ON t.account_id = a.id
WHERE a.account_number = '5312345678'
ORDER BY t.transaction_date DESC;


-- 10. EJEMPLO DE DELETE
-- Se deja comentado para evitar borrar accidentalmente datos.

-- DELETE FROM clients
-- WHERE identification_type = 'CC'
--   AND identification_number = '1234567890';