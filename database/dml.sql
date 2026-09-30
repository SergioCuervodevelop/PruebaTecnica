-- DML - PRUEBA TÉCNICA


-- 1. INSERTAR UN CLIENTE ACTIVO
INSERT INTO clients (
    identification_type,
    identification_number,
    first_name,
    last_name,
    email,
    birth_date,
    status,
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
           'ACTIVE',
           CURRENT_TIMESTAMP,
           CURRENT_TIMESTAMP
       );


-- 2. CONSULTAR TODOS LOS CLIENTES
SELECT *
FROM clients;


-- 3. CONSULTAR UN CLIENTE POR SU IDENTIFICACIÓN
SELECT *
FROM clients
WHERE identification_type = 'CC'
  AND identification_number = '1234567890';


-- 4. ACTUALIZAR INFORMACIÓN DE UN CLIENTE
UPDATE clients
SET first_name = 'Sergio Andres',
    updated_at = CURRENT_TIMESTAMP
WHERE identification_type = 'CC'
  AND identification_number = '1234567890';


-- 5. ELIMINACIÓN LÓGICA DE UN CLIENTE
-- No se elimina físicamente de la base de datos.
UPDATE clients
SET status = 'DELETED',
    updated_at = CURRENT_TIMESTAMP
WHERE identification_type = 'CC'
  AND identification_number = '1234567890';


-- 6. RESTAURAR UN CLIENTE ELIMINADO
UPDATE clients
SET status = 'ACTIVE',
    updated_at = CURRENT_TIMESTAMP
WHERE identification_type = 'CC'
  AND identification_number = '1234567890';


-- 7. CONSULTAR SOLO CLIENTES ACTIVOS
SELECT *
FROM clients
WHERE status = 'ACTIVE';


-- 8. CONSULTAR CLIENTES ELIMINADOS
SELECT *
FROM clients
WHERE status = 'DELETED';


-- 9. INSERTAR UNA CUENTA DE AHORROS
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
                 AND status = 'ACTIVE'
           )
       );


-- 10. INSERTAR UNA CUENTA CORRIENTE
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
           'CHECKING',
           '3312345678',
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
                 AND status = 'ACTIVE'
           )
       );


-- 11. CONSULTAR TODAS LAS CUENTAS
SELECT *
FROM accounts;


-- 12. CONSULTAR LAS CUENTAS DE UN CLIENTE
SELECT a.*
FROM accounts a
         JOIN clients c
              ON a.client_id = c.id
WHERE c.identification_type = 'CC'
  AND c.identification_number = '1234567890';


-- 13. INSERTAR UN DEPÓSITO
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


-- 14. ACTUALIZAR SALDOS DESPUÉS DEL DEPÓSITO
UPDATE accounts
SET balance = balance + 50000.00,
    available_balance = available_balance + 50000.00,
    updated_at = CURRENT_TIMESTAMP
WHERE account_number = '5312345678';


-- 15. INSERTAR UN RETIRO
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
           'WITHDRAWAL',
           'DEBIT',
           10000.00,
           CURRENT_TIMESTAMP,
           NULL,
           NULL,
           (
               SELECT id
               FROM accounts
               WHERE account_number = '5312345678'
           )
       );


-- 16. ACTUALIZAR SALDOS DESPUÉS DEL RETIRO
UPDATE accounts
SET balance = balance - 10000.00,
    available_balance = available_balance - 10000.00,
    updated_at = CURRENT_TIMESTAMP
WHERE account_number = '5312345678';


-- 17. INSERTAR MOVIMIENTO DÉBITO DE UNA TRANSFERENCIA
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
           'TRANSFER',
           'DEBIT',
           10000.00,
           CURRENT_TIMESTAMP,
           (
               SELECT id
               FROM accounts
               WHERE account_number = '3312345678'
           ),
           'TRANSFER-001',
           (
               SELECT id
               FROM accounts
               WHERE account_number = '5312345678'
           )
       );


-- 18. INSERTAR MOVIMIENTO CRÉDITO DE UNA TRANSFERENCIA
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
           'TRANSFER',
           'CREDIT',
           10000.00,
           CURRENT_TIMESTAMP,
           (
               SELECT id
               FROM accounts
               WHERE account_number = '3312345678'
           ),
           'TRANSFER-001',
           (
               SELECT id
               FROM accounts
               WHERE account_number = '3312345678'
           )
       );


-- 19. ACTUALIZAR CUENTA ORIGEN DESPUÉS DE LA TRANSFERENCIA
UPDATE accounts
SET balance = balance - 10000.00,
    available_balance = available_balance - 10000.00,
    updated_at = CURRENT_TIMESTAMP
WHERE account_number = '5312345678';


-- 20. ACTUALIZAR CUENTA DESTINO DESPUÉS DE LA TRANSFERENCIA
UPDATE accounts
SET balance = balance + 10000.00,
    available_balance = available_balance + 10000.00,
    updated_at = CURRENT_TIMESTAMP
WHERE account_number = '3312345678';


-- 21. CONSULTAR MOVIMIENTOS DE UNA CUENTA
SELECT t.*
FROM transactions t
         JOIN accounts a
              ON t.account_id = a.id
WHERE a.account_number = '5312345678'
ORDER BY t.transaction_date DESC;


-- 22. INACTIVAR UNA CUENTA
UPDATE accounts
SET status = 'INACTIVE',
    updated_at = CURRENT_TIMESTAMP
WHERE account_number = '5312345678';


-- 23. ACTIVAR NUEVAMENTE UNA CUENTA INACTIVA
UPDATE accounts
SET status = 'ACTIVE',
    updated_at = CURRENT_TIMESTAMP
WHERE account_number = '5312345678'
  AND status = 'INACTIVE';


-- 24. CANCELAR UNA CUENTA
-- Según la lógica del proyecto, la cuenta solo debe cancelarse si su saldo es 0.
UPDATE accounts
SET status = 'CANCELLED',
    updated_at = CURRENT_TIMESTAMP
WHERE account_number = '5312345678'
  AND balance = 0;


-- 25. RESTAURAR UNA CUENTA CANCELADA
UPDATE accounts
SET status = 'ACTIVE',
    updated_at = CURRENT_TIMESTAMP
WHERE account_number = '5312345678'
  AND status = 'CANCELLED';


-- 26. CONSULTAR CUENTAS ACTIVAS
SELECT *
FROM accounts
WHERE status = 'ACTIVE';


-- 27. CONSULTAR CUENTAS CANCELADAS
SELECT *
FROM accounts
WHERE status = 'CANCELLED';


-- 28. CONSULTAR RESUMEN DE CLIENTES Y CANTIDAD DE CUENTAS
SELECT
    c.identification_type,
    c.identification_number,
    c.first_name,
    c.last_name,
    c.status,
    COUNT(a.id) AS account_count
FROM clients c
         LEFT JOIN accounts a
                   ON c.id = a.client_id
GROUP BY
    c.id,
    c.identification_type,
    c.identification_number,
    c.first_name,
    c.last_name,
    c.status
ORDER BY c.id;


-- 29. DELETE FÍSICO
-- Se deja comentado porque el proyecto usa eliminación lógica para clientes.
--
-- DELETE FROM clients
-- WHERE identification_type = 'CC'
--   AND identification_number = '1234567890';