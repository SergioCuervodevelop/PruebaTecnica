
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


-- 3. ACTUALIZAR UN CLIENTE
UPDATE clients
SET first_name = 'Sergio Andres',
    updated_at = CURRENT_TIMESTAMP
WHERE identification_number = '1234567890';


-- 4. INSERTAR UNA CUENTA DE AHORROS
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
           1
       );


-- 5. CONSULTAR CUENTAS
SELECT *
FROM accounts;


-- 6. INSERTAR UN DEPÓSITO
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
           1
       );


-- 7. ACTUALIZAR SALDOS DESPUÉS DEL DEPÓSITO
UPDATE accounts
SET balance = balance + 50000.00,
    available_balance = available_balance + 50000.00,
    updated_at = CURRENT_TIMESTAMP
WHERE id = 1;


-- 8. CONSULTAR MOVIMIENTOS DE UNA CUENTA
SELECT *
FROM transactions
WHERE account_id = 1
ORDER BY transaction_date DESC;


-- 9. EJEMPLO DE DELETE
-- Se deja comentado para evitar borrar accidentalmente datos.
-- DELETE FROM clients
-- WHERE identification_number = '1234567890';