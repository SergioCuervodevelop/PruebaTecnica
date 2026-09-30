CREATE TABLE clients (
                         id BIGSERIAL PRIMARY KEY,

                         identification_type VARCHAR(255) NOT NULL,
                         identification_number VARCHAR(255) NOT NULL,

                         first_name VARCHAR(255) NOT NULL,
                         last_name VARCHAR(255) NOT NULL,

                         email VARCHAR(255) NOT NULL UNIQUE,

                         birth_date DATE NOT NULL,

                         status VARCHAR(255) NOT NULL DEFAULT 'ACTIVE',

                         created_at TIMESTAMP NOT NULL,
                         updated_at TIMESTAMP,

                         CONSTRAINT uk_clients_identification
                             UNIQUE (
                                     identification_type,
                                     identification_number
                                 ),

                         CONSTRAINT chk_identification_type
                             CHECK (
                                 identification_type IN (
                                                         'CC',
                                                         'CE',
                                                         'PA'
                                     )
                                 ),

                         CONSTRAINT chk_client_status
                             CHECK (
                                 status IN (
                                            'ACTIVE',
                                            'DELETED'
                                     )
                                 )
);


CREATE TABLE accounts (
                          id BIGSERIAL PRIMARY KEY,

                          account_type VARCHAR(255) NOT NULL,

                          account_number VARCHAR(255) NOT NULL UNIQUE,

                          status VARCHAR(255) NOT NULL DEFAULT 'ACTIVE',

                          balance NUMERIC(38,2) NOT NULL DEFAULT 0,

                          available_balance NUMERIC(38,2) NOT NULL DEFAULT 0,

                          gmf_exempt BOOLEAN NOT NULL DEFAULT FALSE,

                          created_at TIMESTAMP NOT NULL,
                          updated_at TIMESTAMP NOT NULL,

                          client_id BIGINT NOT NULL,

                          CONSTRAINT fk_account_client
                              FOREIGN KEY (client_id)
                                  REFERENCES clients(id),

                          CONSTRAINT chk_account_type
                              CHECK (
                                  account_type IN (
                                                   'SAVINGS',
                                                   'CHECKING'
                                      )
                                  ),

                          CONSTRAINT chk_account_status
                              CHECK (
                                  status IN (
                                             'ACTIVE',
                                             'INACTIVE',
                                             'CANCELLED'
                                      )
                                  ),

                          CONSTRAINT chk_account_balance
                              CHECK (
                                  balance >= 0
                                  ),

                          CONSTRAINT chk_available_balance
                              CHECK (
                                  available_balance >= 0
                                  ),

                          CONSTRAINT chk_account_number_length
                              CHECK (
                                  LENGTH(account_number) = 10
                                  ),

                          CONSTRAINT chk_account_number_prefix
                              CHECK (
                                  (account_type = 'SAVINGS'
                                      AND account_number LIKE '53%')
                                      OR
                                  (account_type = 'CHECKING'
                                      AND account_number LIKE '33%')
                                  )
);


CREATE TABLE transactions (
                              id BIGSERIAL PRIMARY KEY,

                              transaction_type VARCHAR(255) NOT NULL,

                              movement_type VARCHAR(255) NOT NULL,

                              amount NUMERIC(38,2) NOT NULL,

                              transaction_date TIMESTAMP NOT NULL,

                              destination_account_id BIGINT,

                              transfer_id VARCHAR(255),

                              account_id BIGINT NOT NULL,

                              CONSTRAINT fk_transaction_account
                                  FOREIGN KEY (account_id)
                                      REFERENCES accounts(id),

                              CONSTRAINT fk_transaction_destination_account
                                  FOREIGN KEY (destination_account_id)
                                      REFERENCES accounts(id),

                              CONSTRAINT chk_transaction_type
                                  CHECK (
                                      transaction_type IN (
                                                           'DEPOSIT',
                                                           'WITHDRAWAL',
                                                           'TRANSFER'
                                          )
                                      ),

                              CONSTRAINT chk_movement_type
                                  CHECK (
                                      movement_type IN (
                                                        'DEBIT',
                                                        'CREDIT'
                                          )
                                      ),

                              CONSTRAINT chk_transaction_amount
                                  CHECK (
                                      amount > 0
                                      )
);


CREATE INDEX idx_accounts_client_id
    ON accounts(client_id);


CREATE INDEX idx_transactions_account_id
    ON transactions(account_id);


CREATE INDEX idx_transactions_transfer_id
    ON transactions(transfer_id);


CREATE INDEX idx_clients_identification
    ON clients(
               identification_type,
               identification_number
        );