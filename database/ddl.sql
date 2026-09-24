CREATE TABLE clients (
                         id BIGSERIAL PRIMARY KEY,
                         identification_type VARCHAR(255) NOT NULL,
                         identification_number VARCHAR(255) NOT NULL,
                         first_name VARCHAR(255) NOT NULL,
                         last_name VARCHAR(255) NOT NULL,
                         email VARCHAR(255) NOT NULL UNIQUE,
                         birth_date DATE NOT NULL,
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
                                 )
);

CREATE TABLE accounts (
                          id BIGSERIAL PRIMARY KEY,
                          account_type VARCHAR(255) NOT NULL,
                          account_number VARCHAR(255) NOT NULL UNIQUE,
                          status VARCHAR(255) NOT NULL,
                          balance NUMERIC(38,2) NOT NULL,
                          available_balance NUMERIC(38,2) NOT NULL,
                          gmf_exempt BOOLEAN NOT NULL,
                          created_at TIMESTAMP NOT NULL,
                          updated_at TIMESTAMP NOT NULL,
                          client_id BIGINT,

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
                              account_id BIGINT,

                              CONSTRAINT fk_transaction_account
                                  FOREIGN KEY (account_id)
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
                                      )
);