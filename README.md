# Prueba Técnica - API de Productos Financieros

API REST desarrollada con Java y Spring Boot para la administración de clientes, productos financieros y transacciones.

El proyecto implementa una arquitectura hexagonal con separación entre dominio, casos de uso e infraestructura.

## Tecnologías utilizadas

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate
- PostgreSQL
- Maven
- Docker
- Docker Compose
- JUnit
- Mockito
- Postman
- Git / GitHub

## Arquitectura

El proyecto utiliza Arquitectura Hexagonal (Ports and Adapters).

La aplicación está dividida principalmente en:

### Domain

Contiene la lógica y las reglas principales del negocio.

Ejemplos:

- Client
- Account
- Transaction
- Enums
- Excepciones de dominio

### Application

Contiene los casos de uso y servicios de la aplicación.

Los puertos de entrada representan las operaciones que puede realizar la aplicación.

Ejemplos:

- CreateClientUseCase
- CreateAccountUseCase
- CreateTransactionUseCase
- TransferMoneyUseCase
- CancelAccountUseCase

Los puertos de salida definen las operaciones que la aplicación necesita de sistemas externos.

Ejemplos:

- ClientRepositoryPort
- AccountRepositoryPort
- TransactionRepositoryPort
- AccountNumberGeneratorPort

### Infrastructure

Contiene los adaptadores que permiten comunicar la aplicación con tecnologías externas.

Incluye:

- Controladores REST
- DTOs
- Mappers
- Persistencia JPA
- Repositorios Spring Data
- Configuración de Beans
- Manejo global de excepciones

## Funcionalidades

### Clientes

- Crear cliente.
- Consultar cliente.
- Actualizar cliente.
- Eliminar cliente.
- Validar mayoría de edad.
- Evitar eliminar clientes con productos financieros asociados.
- Validar información de entrada.
- Consultar resumen de clientes y cuentas.

### Cuentas

La aplicación maneja:

- Cuenta de ahorros (`SAVINGS`).
- Cuenta corriente (`CHECKING`).

Las cuentas poseen:

- Número único de 10 dígitos.
- Prefijo `53` para cuentas de ahorro.
- Prefijo `33` para cuentas corrientes.
- Saldo.
- Saldo disponible.
- Estado.
- Fecha de creación y actualización.
- Cliente propietario.

Estados disponibles:

- `ACTIVE`
- `INACTIVE`
- `CANCELLED`

Una cuenta solamente puede cancelarse cuando su saldo es cero.

### Transacciones

La aplicación permite:

- Depósitos.
- Retiros.
- Transferencias.

Tipos:

- `DEPOSIT`
- `WITHDRAWAL`
- `TRANSFER`

Movimientos:

- `CREDIT`
- `DEBIT`

Las transferencias generan dos movimientos relacionados mediante un mismo `transferId`:

- Débito en la cuenta origen.
- Crédito en la cuenta destino.

Los depósitos y retiros no necesitan `transferId`.

## Principios SOLID

El proyecto aplica principios SOLID.

### Single Responsibility Principle

Cada clase posee una responsabilidad específica.

Por ejemplo, los controladores reciben solicitudes HTTP, los servicios ejecutan casos de uso y los adaptadores de persistencia se encargan del acceso a datos.

### Open/Closed Principle

La utilización de interfaces y puertos permite agregar nuevas implementaciones sin modificar directamente la lógica del dominio.

### Liskov Substitution Principle

Las implementaciones de los puertos pueden sustituirse siempre que respeten el contrato definido por sus interfaces.

### Interface Segregation Principle

Los casos de uso se encuentran separados en interfaces específicas según cada operación.

### Dependency Inversion Principle

La lógica de aplicación depende de abstracciones como:

- AccountRepositoryPort
- ClientRepositoryPort
- TransactionRepositoryPort

y no directamente de Spring Data JPA.

## Patrones de diseño

Se utilizan diferentes patrones y conceptos de diseño:

### Repository Pattern

Los puertos de repositorio abstraen el acceso a PostgreSQL.

### Adapter Pattern

Los adaptadores de persistencia implementan los puertos definidos por la aplicación.

### Dependency Injection

Spring se encarga de proporcionar las dependencias necesarias a los componentes.

### DTO Pattern

Los DTOs permiten separar los objetos utilizados por la API de los objetos internos del dominio.

### Mapper Pattern

Los mappers transforman DTOs, entidades de persistencia y objetos de dominio.

## ACID y transacciones

Las operaciones que modifican información financiera se ejecutan utilizando transacciones de base de datos.

Spring utiliza `@Transactional` para garantizar que operaciones relacionadas se ejecuten de forma atómica.

Por ejemplo, durante una transferencia:

1. Se valida la cuenta origen.
2. Se valida la cuenta destino.
3. Se descuenta el dinero de la cuenta origen.
4. Se acredita el dinero en la cuenta destino.
5. Se actualizan ambas cuentas.
6. Se generan los movimientos débito y crédito.

Si ocurre una excepción durante la operación, la transacción puede realizar rollback evitando que la información financiera quede parcialmente actualizada.

PostgreSQL proporciona las propiedades ACID:

- Atomicidad.
- Consistencia.
- Aislamiento.
- Durabilidad.

## Base de datos

La aplicación utiliza PostgreSQL.

Las tablas principales son:

- `clients`
- `accounts`
- `transactions`

Los scripts SQL se encuentran en:

```text
database/
├── ddl.sql
└── dml.sql
```

`ddl.sql` contiene la definición de las estructuras de base de datos.

`dml.sql` contiene ejemplos de manipulación y consulta de datos.

## Docker

La aplicación y PostgreSQL pueden ejecutarse utilizando Docker Compose.

Construir e iniciar los contenedores:

```bash
docker compose up --build
```

Detenerlos:

```bash
docker compose down
```

La API estará disponible en:

```text
http://localhost:8080
```

PostgreSQL se ejecuta dentro de Docker en el puerto `5432` y se expone en el host mediante el puerto `5433`.

## Endpoints principales

### Clientes

```text
POST   /api/clients
GET    /api/clients/{id}
PUT    /api/clients/{id}
DELETE /api/clients/{id}
GET    /api/clients/summary
```

### Cuentas

```text
POST   /api/accounts
GET    /api/accounts/{id}
PATCH  /api/accounts/{id}/status
DELETE /api/accounts/{id}
```

Ejemplo para cambiar el estado:

```text
PATCH /api/accounts/1/status?status=INACTIVE
```

### Transacciones

```text
POST /api/transactions
GET  /api/transactions/account/{accountId}
POST /api/transactions/transfer
```

## Ejemplo de depósito

```json
{
  "accountId": 1,
  "transactionType": "DEPOSIT",
  "amount": 50000
}
```

## Ejemplo de transferencia

```json
{
  "sourceAccountId": 1,
  "destinationAccountId": 2,
  "amount": 50000
}
```

## Pruebas

El proyecto contiene pruebas unitarias y pruebas de controladores utilizando JUnit y Mockito.

Entre los componentes probados se encuentran:

- CreateClientService
- DeleteClientService
- CreateAccountService
- CreateTransactionService
- TransferMoneyService
- ClientController
- AccountController
- TransactionController

Para ejecutar las pruebas:

```bash
./mvnw test
```

En Windows:

```powershell
mvnw.cmd test
```

## Ejecución local

Para ejecutar el proyecto sin Docker se requiere PostgreSQL disponible localmente.

La aplicación utiliza variables de entorno cuando están disponibles y valores locales como respaldo.

Ejecutar:

```powershell
mvnw.cmd spring-boot:run
```

## Autor

Sergio Cuervo