# Prueba Técnica - Productos Financieros

Aplicación desarrollada como prueba técnica para la gestión de clientes, cuentas y transacciones financieras.

El backend fue construido con Java y Spring Boot utilizando arquitectura hexagonal. Para la persistencia se utiliza PostgreSQL y se agregó un frontend sencillo en React para probar las principales operaciones desde una interfaz gráfica.

## Tecnologías

### Backend

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate
- PostgreSQL
- Maven
- JUnit
- Mockito

### Frontend

- React
- Vite
- JavaScript
- CSS

### Herramientas

- Docker
- Docker Compose
- Nginx
- Postman
- Git
- GitHub

## Arquitectura

El backend utiliza arquitectura hexagonal para separar la lógica de negocio de las tecnologías externas.

La estructura principal se divide en tres partes:

### Domain

Contiene las entidades y reglas principales del negocio.

Entre ellas:

- Client
- Account
- Transaction
- Enums
- Excepciones de dominio

Esta capa no depende directamente de controladores, bases de datos o frameworks.

### Application

Contiene los casos de uso de la aplicación.

Algunos de los casos de uso implementados son:

- CreateClientUseCase
- UpdateClientUseCase
- DeleteClientUseCase
- CreateAccountUseCase
- GetAccountsByClientUseCase
- CreateTransactionUseCase
- TransferMoneyUseCase
- CancelAccountUseCase

También se encuentran los puertos utilizados para comunicarse con infraestructura, por ejemplo:

- ClientRepositoryPort
- AccountRepositoryPort
- TransactionRepositoryPort
- AccountNumberGeneratorPort

### Infrastructure

Contiene las implementaciones relacionadas con tecnologías externas.

Aquí se encuentran:

- Controladores REST
- DTOs
- Mappers
- Entidades JPA
- Repositorios Spring Data
- Adaptadores de persistencia
- Configuración de Beans
- Configuración de CORS
- Manejo global de excepciones


## Identificadores públicos e internos

La API utiliza identificadores de negocio en lugar de exponer los IDs técnicos de la base de datos.

### Cliente

- `id`: identificador técnico interno generado por la base de datos.
- `identificationNumber`: identificador público utilizado para consultar, actualizar y eliminar clientes.

### Cuenta

- `id`: identificador técnico interno generado por la base de datos.
- `accountNumber`: identificador público utilizado para consultar, modificar y cancelar cuentas.

Las relaciones internas continúan usando los IDs técnicos:

```text
accounts.client_id      -> clients.id
transactions.account_id -> accounts.id
```

De esta manera, los IDs técnicos permanecen internos mientras la API trabaja con `identificationNumber` y `accountNumber`.

## Funcionalidades

### Clientes

Se pueden realizar las siguientes operaciones:

- Crear clientes.
- Consultar clientes.
- Actualizar información.
- Eliminar clientes.
- Consultar un resumen de clientes y cuentas.
- Consultar las cuentas asociadas a un cliente.

Al crear un cliente se valida que sea mayor de edad.

También se evita eliminar un cliente cuando tiene cuentas asociadas.

Se realizan validaciones básicas como:

- Nombre y apellido con mínimo 2 caracteres.
- Correo electrónico válido.
- Tipo y número de identificación obligatorios.
- Fecha de nacimiento obligatoria.

Tipos de identificación permitidos:

- `CC`: Cédula de ciudadanía.
- `PA`: Pasaporte.
- `CE`: Cédula de extranjería.

## Cuentas

La aplicación maneja dos tipos de cuenta:

- `SAVINGS`: cuenta de ahorros.
- `CHECKING`: cuenta corriente.

Cada cuenta contiene:

- Número de cuenta.
- Tipo.
- Saldo.
- Saldo disponible.
- Estado.
- Fecha de creación.
- Fecha de actualización.
- Cliente propietario.

El número de cuenta se genera automáticamente con 10 dígitos.

Los prefijos utilizados son:

- `53` para cuentas de ahorro.
- `33` para cuentas corrientes.

Los estados disponibles son:

- `ACTIVE`
- `INACTIVE`
- `CANCELLED`

Las cuentas de ahorro se crean activas por defecto.

Una cuenta solamente puede cancelarse cuando su saldo es cero.

Las operaciones financieras solo pueden realizarse sobre cuentas que se encuentren activas.

## Transacciones

La aplicación permite realizar:

- Depósitos.
- Retiros.
- Transferencias.

Los tipos de transacción utilizados son:

- `DEPOSIT`
- `WITHDRAWAL`
- `TRANSFER`

Los movimientos pueden ser:

- `CREDIT`
- `DEBIT`

Cuando se realiza una transferencia se generan dos movimientos:

- Un débito en la cuenta origen.
- Un crédito en la cuenta destino.

Ambos movimientos quedan relacionados mediante el mismo `transferId`.

También se puede consultar el historial de movimientos de una cuenta.

## Frontend

El proyecto incluye una interfaz sencilla desarrollada con React y Vite.

Desde el frontend se pueden realizar las principales operaciones del sistema:

- Crear clientes.
- Consultar clientes.
- Editar clientes.
- Eliminar clientes.
- Consultar las cuentas de un cliente.
- Crear cuentas.
- Consultar cuentas.
- Cambiar el estado de una cuenta.
- Cancelar cuentas.
- Realizar depósitos.
- Realizar retiros.
- Realizar transferencias.
- Consultar el historial de movimientos.

El frontend se encuentra en:

```text
frontend/
```

## Principios SOLID

Durante el desarrollo se buscó mantener responsabilidades separadas entre las diferentes capas.

### Single Responsibility Principle

Cada clase tiene una responsabilidad específica.

Por ejemplo:

- Los controladores reciben las solicitudes HTTP.
- Los servicios ejecutan los casos de uso.
- Los adaptadores se encargan de la persistencia.
- Los repositorios abstraen el acceso a los datos.

### Open/Closed Principle

El uso de interfaces permite agregar nuevas implementaciones sin modificar directamente las reglas del dominio.

### Liskov Substitution Principle

Las implementaciones de los puertos pueden sustituirse mientras respeten el contrato definido por la interfaz.

### Interface Segregation Principle

Los casos de uso se encuentran separados según cada operación.

### Dependency Inversion Principle

Los servicios dependen de abstracciones como:

- AccountRepositoryPort
- ClientRepositoryPort
- TransactionRepositoryPort

De esta forma la lógica de negocio no depende directamente de Spring Data JPA.

## Patrones utilizados

### Repository

Se utiliza para abstraer el acceso a la base de datos.

### Adapter

Los adaptadores implementan los puertos definidos por el dominio y la aplicación.

### Dependency Injection

Spring administra las dependencias necesarias para ejecutar los casos de uso.

### DTO

Los DTOs separan la información recibida por la API de los objetos internos del dominio.

### Mapper

Los mappers se utilizan para convertir entre entidades JPA, objetos de dominio y DTOs.

## ACID y manejo de transacciones

Las operaciones financieras que requieren varios cambios en la base de datos utilizan transacciones.

En el caso de una transferencia se realizan varias operaciones:

1. Se busca la cuenta origen.
2. Se busca la cuenta destino.
3. Se valida la operación.
4. Se descuenta el valor de la cuenta origen.
5. Se acredita el valor en la cuenta destino.
6. Se guardan los nuevos saldos.
7. Se registran los movimientos correspondientes.

El servicio de transferencia utiliza `@Transactional`.

Si ocurre un error durante el proceso, Spring puede realizar rollback evitando guardar una transferencia incompleta.

PostgreSQL proporciona las propiedades ACID necesarias para mantener la consistencia de la información.

## Base de datos

Se utiliza PostgreSQL.

Las principales tablas son:

- `clients`
- `accounts`
- `transactions`

También se incluyen scripts SQL dentro del proyecto:

```text
database/
├── ddl.sql
└── dml.sql
```

`ddl.sql` contiene la creación de las estructuras principales.

`dml.sql` contiene ejemplos de inserción, modificación y consulta de información.

## Estructura general

```text
PruebaTecnica/
├── database/
│   ├── ddl.sql
│   └── dml.sql
├── frontend/
│   ├── Dockerfile
│   ├── .dockerignore
│   └── src/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/cuervo/
│   │           ├── application/
│   │           ├── domain/
│   │           ├── infrastructure/
│   │           └── pruebatecnica/
│   └── test/
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

## Endpoints

### Clientes

```text
POST   /api/clients
GET    /api/clients/{identificationNumber}
PUT    /api/clients/{identificationNumber}
DELETE /api/clients/{identificationNumber}
GET    /api/clients/summary
```

### Cuentas

```text
POST   /api/accounts
GET    /api/accounts/{accountNumber}
GET    /api/accounts/client/{identificationNumber}
PATCH  /api/accounts/{accountNumber}/status?status=ACTIVE|INACTIVE
DELETE /api/accounts/{accountNumber}
```

Ejemplo para cambiar el estado de una cuenta:

```text
PATCH /api/accounts/5312345678/status?status=INACTIVE
```

### Transacciones

```text
POST /api/transactions
GET  /api/transactions/account/{accountNumber}
POST /api/transactions/transfer
```

## Ejemplo de depósito

```json
{
  "accountNumber": "5312345678",
  "transactionType": "DEPOSIT",
  "amount": 50000
}
```

## Ejemplo de retiro

```json
{
  "accountNumber": "5312345678",
  "transactionType": "WITHDRAWAL",
  "amount": 20000
}
```

## Ejemplo de transferencia

```json
{
  "sourceAccountNumber": "5312345678",
  "destinationAccountNumber": "3312345678",
  "amount": 50000
}
```

## Pruebas

Se realizaron pruebas con JUnit y Mockito tanto para servicios como para controladores.

Entre las clases probadas se encuentran:

- CreateClientService
- DeleteClientService
- CreateAccountService
- CreateTransactionService
- TransferMoneyService
- ClientController
- AccountController
- TransactionController

Para ejecutar todas las pruebas:

```bash
mvn test
```

También se pueden ejecutar utilizando Maven Wrapper.

En Windows:

```powershell
mvnw.cmd test
```

## Ejecución local del backend

Para ejecutar el backend localmente es necesario tener PostgreSQL disponible.

La configuración se encuentra en:

```text
src/main/resources/application.properties
```

Ejecutar:

```powershell
mvn spring-boot:run
```

o:

```powershell
mvnw.cmd spring-boot:run
```

La API quedará disponible en:

```text
http://localhost:8080
```

## Ejecución del frontend

Entrar a la carpeta:

```powershell
cd frontend
```

Instalar las dependencias:

```powershell
npm install
```

Ejecutar el proyecto:

```powershell
npm run dev
```

Para generar el build:

```powershell
npm run build
```

Vite normalmente inicia el frontend en:

```text
http://localhost:5173
```

Si ese puerto está ocupado puede utilizar otro puerto disponible.

## Docker

El proyecto incluye Docker para los tres componentes principales:

```text
Docker Compose
├── bankapi-postgres   -> PostgreSQL
├── bankapi-app        -> Spring Boot
└── bankapi-frontend   -> React + Nginx
```

Archivos principales:

```text
Dockerfile
docker-compose.yml
frontend/Dockerfile
frontend/.dockerignore
```

Para construir e iniciar los contenedores:

```bash
docker compose up --build
```

Para detenerlos:

```bash
docker compose down
```

Servicios expuestos:

```text
Frontend     -> http://localhost:5173
Backend API  -> http://localhost:8080
PostgreSQL   -> localhost:5433
```

Dentro de la red de Docker, Spring Boot se conecta a PostgreSQL mediante:

```text
jdbc:postgresql://postgres:5432/Bankapi
```

El frontend se construye con Node y se sirve en producción mediante Nginx.

## Git y GitHub

El proyecto utiliza Git para el control de versiones.

Se mantiene `main` como rama principal y se utilizaron ramas `feature/...` para trabajar cambios antes de integrarlos.

Un ejemplo utilizado durante el desarrollo fue:

```text
feature/transaction-validation
```

En esta rama se agregó la validación para impedir que una transferencia fuera procesada utilizando el endpoint normal de depósitos y retiros.

Después de probar los cambios fueron integrados nuevamente a `main`.

## Servicios cloud

Para esta prueba no se integraron servicios cloud. La aplicación está preparada para ejecutarse localmente o mediante Docker.

## Autor

Sergio Cuervo