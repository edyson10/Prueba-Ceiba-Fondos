# Prueba Técnica Ceiba Back End - BTG Funds API 

### Proyecto desarrollado en **Java 17 + Spring Boot + MongoDB**, siguiendo buenas prácticas de desarrollo, clean code, manejo de excepciones, seguridad con JWT, pruebas unitarias, documentación con Swagger y despliegue documentado con AWS CloudFormation.

---

## 1. Objetivo

### La solución implementa una plataforma para que los clientes puedan:

1. Suscribirse a un nuevo fondo.
2. Cancelar la suscripción a un fondo actual.
3. Consultar el historial de transacciones.
4. Recibir notificación por email o SMS según preferencia del usuario.

---

## 2. Reglas de negocio implementadas

- Saldo inicial del cliente: **COP 500.000**
- Cada transacción tiene identificador único
- Cada fondo tiene un monto mínimo de vinculación
- Al cancelar una suscripción, el valor se retorna al cliente
- Si no hay saldo suficiente, se responde:
  **No tiene saldo disponible para vincularse al fondo <Nombre del fondo>**

---

## 3. Tecnologías usadas

- Java 17
- Spring Boot
- Spring Web
- Spring Security
- Spring Validation
- Spring Data MongoDB
- JWT
- MapStruct
- Lombok
- JUnit 5
- Mockito
- Swagger / OpenAPI
- Docker
- AWS CloudFormation

---

# 4. Arquitectura del proyecto

### El proyecto se organizó por capas para mantener separación de responsabilidades:

- `controller`: expone la API REST
- `service`: contratos de negocio
- `service.impl`: implementación de reglas de negocio
- `repository`: acceso a datos
- `model`: entidades del dominio
- `dto`: request y response
- `mapper`: transformación entre entidades y DTOs
- `exception`: manejo global de errores
- `security`: autenticación y autorización con JWT
- `config`: configuración general, seed inicial y Swagger
- `infra/cloudformation`: infraestructura como código en AWS

---

# 5. Estructura del proyecto

```text
Prueba-ceiba
├── Dockerfile
├── docker-compose.yml
├── pom.xml
├── README.md
├── infra
│   └── cloudformation
│       └── backend-ec2.yml
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── btg
    │   │           └── funds
    │   │               ├── BtgFundsApiApplication.java
    │   │               ├── config
    │   │               │   ├── DataInitializer.java
    │   │               │   ├── OpenApiConfig.java
    │   │               │   └── PasswordConfig.java
    │   │               ├── controller
    │   │               │   ├── AuthController.java
    │   │               │   ├── ClientController.java
    │   │               │   ├── FundController.java
    │   │               │   └── SubscriptionController.java
    │   │               ├── dto
    │   │               │   ├── request
    │   │               │   │   ├── CancelSubscriptionRequest.java
    │   │               │   │   ├── ClientRegisterRequest.java
    │   │               │   │   ├── LoginRequest.java
    │   │               │   │   └── SubscribeFundRequest.java
    │   │               │   └── response
    │   │               │       ├── ApiErrorResponse.java
    │   │               │       ├── ApiResponse.java
    │   │               │       ├── AuthResponse.java
    │   │               │       ├── ClientResponse.java
    │   │               │       ├── FundResponse.java
    │   │               │       ├── SubscriptionResponse.java
    │   │               │       └── TransactionResponse.java
    │   │               ├── exception
    │   │               │   ├── BusinessException.java
    │   │               │   ├── GlobalExceptionHandler.java
    │   │               │   ├── InsufficientBalanceException.java
    │   │               │   └── ResourceNotFoundException.java
    │   │               ├── mapper
    │   │               │   ├── ClientMapper.java
    │   │               │   ├── FundMapper.java
    │   │               │   ├── SubscriptionMapper.java
    │   │               │   └── TransactionMapper.java
    │   │               ├── model
    │   │               │   ├── Client.java
    │   │               │   ├── Fund.java
    │   │               │   ├── Subscription.java
    │   │               │   ├── Transaction.java
    │   │               │   └── enums
    │   │               │       ├── NotificationPreference.java
    │   │               │       ├── Role.java
    │   │               │       ├── SubscriptionStatus.java
    │   │               │       └── TransactionType.java
    │   │               ├── repository
    │   │               │   ├── ClientRepository.java
    │   │               │   ├── FundRepository.java
    │   │               │   ├── SubscriptionRepository.java
    │   │               │   └── TransactionRepository.java
    │   │               ├── security
    │   │               │   ├── CustomUserDetailsService.java
    │   │               │   ├── JwtAuthenticationFilter.java
    │   │               │   ├── JwtService.java
    │   │               │   └── SecurityConfig.java
    │   │               └── service
    │   │                   ├── AuthService.java
    │   │                   ├── ClientService.java
    │   │                   ├── FundService.java
    │   │                   ├── NotificationService.java
    │   │                   ├── SubscriptionService.java
    │   │                   └── impl
    │   │                       ├── AuthServiceImpl.java
    │   │                       ├── ClientServiceImpl.java
    │   │                       ├── FundServiceImpl.java
    │   │                       ├── NotificationServiceImpl.java
    │   │                       └── SubscriptionServiceImpl.java
    │   └── resources
    │       └── application.yml
    └── test
        └── java
            └── com
                └── btg
                    └── funds
                        ├── controller
                        │   └── AuthControllerTest.java
                        └── service
                            └── SubscriptionServiceImplTest.java
```

## 6. Requisitos previos

### Tener instalado:

- Java 17
- Maven
- Docker
- Git
- 
## 7. Ejecución local

### Opción 1: backend local + Mongo en Docker

### 1. Levantar MongoDB

```bash
docker run -d -p 27017:27017 --name mongo-local mongo:7
```

### 2. Ejecutar la aplicación

```bash
mvn clean package -DskipTests
mvn spring-boot:run
``` 
   
## Opción 2: levantar todo con Docker

### 1. Construir y ejecutar
```bash
docker compose build --no-cache
docker compose up
```

# 8. Configuración principal
```bash
application.yml
```

## 9. Usuarios de prueba

### La aplicación precarga dos usuarios:

```bash
Administrador
email: admin@btg.com
password: Admin123
```
```bash
Cliente
email: cliente@btg.com
password: Cliente123
```

## 10. Fondos precargados

    ID	Nombre	Monto mínimo	Categoría
    1	FPV_BTG_PACTUAL_RECAUDADORA	75000	FPV
    2	FPV_BTG_PACTUAL_ECOPETROL	125000	FPV
    3	DEUDAPRIVADA	50000	FIC
    4	FDO-ACCIONES	250000	FIC
    5	FPV_BTG_PACTUAL_DINAMICA	100000	FPV

## 11. Swagger

### Una vez levantado el proyecto:

```bash
http://localhost:8080/swagger-ui.html
```

## 12. Endpoints principales

- ### Registro
```bash
POST /api/v1/auth/register
```

### Body:

```bash
{
"fullName": "Edyson Leal",
"email": "edyson@test.com",
"phone": "3001234567",
"password": "123456",
"notificationPreference": "EMAIL"
}
```

- ### Login
```bash
POST /api/v1/auth/login
```

### Body:
```bash
{
"email": "cliente@btg.com",
"password": "Cliente123"
}
```

- ### Consultar fondos
```bash
GET /api/v1/funds
```

- ### Consultar cliente autenticado
```bash
GET /api/v1/clients/me
```

- ### Suscribirse a un fondo
```bash
POST /api/v1/subscriptions
```

### Body:
```bash
{
"clientId": "ID_DEL_CLIENTE",
"fundId": "1",
"amount": 75000
}
```

- ### Cancelar suscripción
```bash
POST /api/v1/subscriptions/cancel
```

### Body:
```bash
{
"subscriptionId": "ID_DE_LA_SUSCRIPCION"
}
```

- ### Ver historial
```bash
GET /api/v1/subscriptions/transactions/{clientId}
```

## 13. Manejo de errores

### La aplicación maneja:

- Validaciones de entrada
- Cliente no encontrado
- Fondo no encontrado
- Suscripción ya existente
- Saldo insuficiente
- Credenciales inválidas
- Errores internos

### Ejemplo de error:

```bash
{
"statusCode": 400,
"status": "BAD_REQUEST",
"message": "No tiene saldo disponible para vincularse al fondo FDO-ACCIONES",
"path": "/api/v1/subscriptions",
"timestamp": "2026-03-31T10:00:00"
}
```

## 14. Pruebas unitarias

### Ejecutar:

```bash
mvn test
```

### Se incluyeron pruebas para:

- login
- suscripción exitosa
- error por saldo insuficiente

## 15. Seguridad implementada

- Autenticación con JWT
- Autorización por roles
- Contraseñas encriptadas con BCrypt
- Endpoints protegidos con Spring Security

### Roles:
```bash
ADMIN
CLIENT
```

## 17. Despliegue en AWS con CloudFormation

### La solución incluye infraestructura como código mediante AWS CloudFormation en:
```bash
infra/cloudformation/backend-ec2.yml
```

### Este template crea:

- una instancia EC2
- un Security Group
- instalación de Docker mediante UserData
- un IAM Role básico para futura integración con CloudWatch

### 17.1 ¿Qué es CloudFormation?

CloudFormation no es una instancia EC2; es el servicio de AWS que permite definir infraestructura como código usando archivos YAML o JSON.

En este proyecto, CloudFormation se usa para automatizar la creación del entorno de despliegue.

### 17.2 Crear una Key Pair

#### En AWS Console:

- Ir a EC2
- Entrar a Key Pairs
- Crear una nueva Key Pair
- Guardar el archivo .pem

### 17.3 Crear el stack desde AWS CLI
```bash
aws cloudformation create-stack \
--stack-name btg-funds-api-stack \
--template-body file://infra/cloudformation/backend-ec2.yml \
--parameters ParameterKey=KeyName,ParameterValue=btg-keypair \
--capabilities CAPABILITY_NAMED_IAM
```
### 17.4 Consultar el estado del stack
```bash
aws cloudformation describe-stacks --stack-name btg-funds-api-stack
```
#### Esperar estado:
```bash
CREATE_COMPLETE
```

### 17.5 Obtener IP pública
```bash
aws cloudformation describe-stacks \
--stack-name btg-funds-api-stack \
--query "Stacks[0].Outputs"
```

### 17.6 Conectarse por SSH

1. Linux/Mac:

- chmod 400 btg-keypair.pem
- ssh -i btg-keypair.pem ec2-user@PUBLIC_IP

2. Windows con PuTTY:

- Convertir el .pem a .ppk usando PuTTYgen
- Abrir PuTTY
- Host: ec2-user@PUBLIC_IP
- Puerto: 22
- En Connection > SSH > Auth, cargar el .ppk
- Abrir sesión

### 17.7 Desplegar el backend en la EC2

#### Instalar Docker si fuera necesario
```bash
sudo dnf update -y
sudo dnf install -y docker git
sudo systemctl enable docker
sudo systemctl start docker
sudo usermod -aG docker ec2-user
```

#### Cerrar y abrir sesión nuevamente.

### Clonar el repositorio
```bash
git clone https://github.com/edyson10/Prueba-Ceiba-Fondos.git
```
```bash
cd Prueba-Ceiba-Fondos
```

### Construir imagen
```bash
docker build -t btg-funds-api .
```

### Levantar Mongo en Docker
```bash
docker run -d -p 27017:27017 --name mongo mongo:7
```

### Ejecutar el backend
```bash
docker run -d \
--name btg-funds-api \
-p 8080:8080 \
-e MONGODB_URI="mongodb://localhost:27017/btg_funds" \
-e JWT_SECRET="12345678901234567890123456789012" \
-e JWT_EXPIRATION_MS="86400000" \
btg-funds-api
```

### 17.8 Acceso a la API

#### Esta es la URL del servicio en AWS para realizar las pruebas
```bash
http://18.219.9.54:8080/swagger-ui/index.html
```

## Autor

Edyson Fabian Leal  
Backend Developer – Java / Spring Boot  
📧 edysonleal3@gmail.com  
🔗 https://www.linkedin.com/in/edyson-leal/