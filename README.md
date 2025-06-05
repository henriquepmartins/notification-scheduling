# Agendador de Notificações

Este projeto foi desenvolvido como parte de um desafio técnico para demonstrar habilidades em desenvolvimento Java e Spring Boot. Seu objetivo principal é permitir o cadastro de notificações (e‐mail e SMS) para envio posterior, seguindo um agendamento predefinido.

---
## Visão Geral

O **Agendador de Notificações** permite:

- Registrar notificações pendentes (e-mail e SMS) com data e hora agendadas.
- Consultar detalhes de uma notificação pelo identificador único.
- Cancelar notificações agendadas antes de seu disparo.

Uma aplicação em segundo plano (scheduler) fica responsável por verificar periodicamente o banco de dados e disparar as notificações cujo horário de evento tenha sido atingido. Esta parte do envio (e-mail, SMS) pode ser facilmente estendida para diferentes provedores ou integrada a serviços externos.

---

## Tecnologias e Dependências

- **Linguagem:** Java 17 (ou superior)
- **Build Tool:** Maven 3.8.x
- **Framework Web:** Spring Boot 3.x  
- **Banco de Dados:**  
  - PostgreSQL (por meio de container Docker)  
  - H2 (para testes de integração, se aplicável)  
- **Containerização:** Docker e Docker Compose  
- **Bibliotecas Principais:**  
  - **Spring Data JPA** (integração com banco de dados)  
  - **Spring Web** (camada REST)  
  - **Spring Scheduler** (agendamento de tarefas)  
  - **Hibernate Validator** (validação de DTOs)  
  - **Lombok** (redução de boilerplate)  
- **Testes:** JUnit 5, Mockito  

---

## Pré-requisitos

Antes de começar, certifique‐se de ter instalado em sua máquina:

1. **Java 17 (ou superior)**  
2. **Maven 3.8.x**  
3. **Docker** (para subir o container do PostgreSQL)  
4. **Docker Compose**  
5. (Opcional) **Insomnia/Postman** — para testar endpoints  

---

## Instalação e Execução

1. **Clone o repositório**  
   ```bash
   git clone https://github.com/henriquepmartins/notification-scheduling.git
   cd notification-scheduling
   ```

2. **Configuração do Docker Compose**  
   Dentro do diretório raiz do projeto, certifique‐se de que o arquivo `docker-compose.yml` contenha as configurações corretas para o serviço de banco de dados. Um exemplo mínimo:

   ```yaml
   version: '3.8'
   services:
     db:
       image: postgres:14-alpine
       container_name: notification-db
       environment:
         POSTGRES_USER: usuario
         POSTGRES_PASSWORD: senha
         POSTGRES_DB: agendamentos
       ports:
         - "5432:5432"
       volumes:
         - db-data:/var/lib/postgresql/data

   volumes:
     db-data:
   ```

3. **Inicie os containers Docker**  
   ```bash
   docker-compose up --build -d
   ```  
   - O serviço PostgreSQL ficará disponível em `localhost:5432`.  
   - Caso você deseje usar outro host/porta, ajuste as variáveis de ambiente e a configuração em `application.properties` conforme indicado a seguir.

4. **Ajuste das configurações de conexão (caso necessário)**  
   No arquivo `src/main/resources/application.properties`, revise as propriedades:

   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/agendamentos
   spring.datasource.username=usuario
   spring.datasource.password=senha
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   spring.jpa.properties.hibernate.format_sql=true
   ```

   Caso queira outro usuário, senha ou banco, altere `usuario`, `senha` e `agendamentos` conforme seu ambiente.

5. **Compile e execute a aplicação**  
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```  
   - A aplicação ficará disponível em `http://localhost:8080`.

---

## Configuração de Variáveis de Ambiente (Opcional)

Se preferir externalizar as credenciais de banco, crie um arquivo `.env` na raiz do projeto:

```env
DB_URL=jdbc:postgresql://localhost:5432/agendamentos
DB_USERNAME=usuario
DB_PASSWORD=senha
```

Então referencie essas variáveis em `application.properties`:

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```

---

## Endpoints da API

Todas as rotas abaixo são acessíveis via HTTP em `http://localhost:8080/agendamento`.

### Cadastrar Notificação Pendente

- **URL:** `POST /agendamento`
- **Descrição:** Registra uma nova notificação para envio futuro.
- **Headers obrigatórios:**
  - `Content-Type: application/json`
- **Payload (JSON):**

  | Campo                  | Tipo       | Descrição                                                                 |
  | ---------------------- | ---------- | -------------------------------------------------------------------------- |
  | `emailDestinatario`    | `String`   | **Obrigatório.** E-mail do destinatário da notificação.                   |
  | `telefoneDestinatario` | `String`   | **Obrigatório.** Telefone (com código de país) do destinatário SMS.        |
  | `mensagem`             | `String`   | **Obrigatório.** Texto da notificação.                                     |
  | `dataHoraEvento`       | `String`   | **Obrigatório.** Data e hora para disparo no formato `dd-MM-yyyy HH:mm:ss`. |

- **Exemplo de requisição:**
  ```http
  POST /agendamento HTTP/1.1
  Host: localhost:8080
  Content-Type: application/json

  {
    "emailDestinatario": "usuario@exemplo.com",
    "telefoneDestinatario": "+5511999998888",
    "mensagem": "Sua reunião começa em 15 minutos!",
    "dataHoraEvento": "10-06-2025 15:30:00"
  }
  ```
- **Resposta de sucesso (HTTP 201 Created):**
  ```json
  {
    "id": "c1a2b3d4-e5f6-7890-abcd-1234efgh5678",
    "emailDestinatario": "usuario@exemplo.com",
    "telefoneDestinatario": "+5511999998888",
    "mensagem": "Sua reunião começa em 15 minutos!",
    "dataHoraEvento": "2025-06-10T15:30:00",
    "status": "PENDENTE",
    "dataHoraCriacao": "2025-06-05T18:45:12"
  }
  ```

### Buscar Notificação por ID

- **URL:** `GET /agendamento/{id}`
- **Descrição:** Retorna os detalhes de uma notificação agendada a partir do seu identificador.
- **Parâmetros de rota:**

  | Parâmetro | Tipo     | Descrição                                          |
  | --------- | -------- | -------------------------------------------------- |
  | `id`      | `String` | **Obrigatório.** ID UUID da notificação cadastrada. |

- **Exemplo de requisição:**
  ```http
  GET /agendamento/c1a2b3d4-e5f6-7890-abcd-1234efgh5678 HTTP/1.1
  Host: localhost:8080
  ```
- **Resposta de sucesso (HTTP 200 OK):**
  ```json
  {
    "id": "c1a2b3d4-e5f6-7890-abcd-1234efgh5678",
    "emailDestinatario": "usuario@exemplo.com",
    "telefoneDestinatario": "+5511999998888",
    "mensagem": "Sua reunião começa em 15 minutos!",
    "dataHoraEvento": "2025-06-10T15:30:00",
    "status": "PENDENTE",
    "dataHoraCriacao": "2025-06-05T18:45:12"
  }
  ```
- **Resposta quando não encontrado (HTTP 404 Not Found):**
  ```json
  {
    "timestamp": "2025-06-05T18:50:00",
    "status": 404,
    "error": "Not Found",
    "message": "Notificação com ID c1a2b3d4-e5f6-7890-abcd-1234efgh5678 não encontrada.",
    "path": "/agendamento/c1a2b3d4-e5f6-7890-abcd-1234efgh5678"
  }
  ```

### Cancelar Notificação por ID

- **URL:** `DELETE /agendamento/{id}`
- **Descrição:** Cancela uma notificação que ainda não foi disparada.
- **Parâmetros de rota:**

  | Parâmetro | Tipo     | Descrição                                          |
  | --------- | -------- | -------------------------------------------------- |
  | `id`      | `String` | **Obrigatório.** ID UUID da notificação cadastrado. |

- **Exemplo de requisição:**
  ```http
  DELETE /agendamento/c1a2b3d4-e5f6-7890-abcd-1234efgh5678 HTTP/1.1
  Host: localhost:8080
  ```
- **Resposta de sucesso (HTTP 204 No Content):**  
  - Sem corpo na resposta, apenas o código 204 indica que a notificação foi cancelada.
- **Resposta quando não encontrado (HTTP 404 Not Found):**
  ```json
  {
    "timestamp": "2025-06-05T18:55:00",
    "status": 404,
    "error": "Not Found",
    "message": "Notificação com ID c1a2b3d4-e5f6-7890-abcd-1234efgh5678 não encontrada.",
    "path": "/agendamento/c1a2b3d4-e5f6-7890-abcd-1234efgh5678"
  }
  ```

---

## Exemplo de Payload e Respostas

1. **Exemplo Genérico de Payload JSON para Cadastro**  
   ```json
   {
     "emailDestinatario": "fulano@exemplo.com",
     "telefoneDestinatario": "+5511987654321",
     "mensagem": "Este é um alerta de teste!",
     "dataHoraEvento": "15-06-2025 10:00:00"
   }
   ```

2. **Exemplo de Resposta ao Cadastrar (HTTP 201)**  
   ```json
   {
     "id": "a9b8c7d6-e5f4-3210-ijkl-mnopqrstuvwx",
     "emailDestinatario": "fulano@exemplo.com",
     "telefoneDestinatario": "+5511987654321",
     "mensagem": "Este é um alerta de teste!",
     "dataHoraEvento": "2025-06-15T10:00:00",
     "status": "PENDENTE",
     "dataHoraCriacao": "2025-06-05T18:40:30"
   }
   ```

3. **Exemplo de Erro de Validação de Payload (HTTP 400 Bad Request)**  
   ```json
   {
     "timestamp": "2025-06-05T18:47:00",
     "status": 400,
     "error": "Bad Request",
     "message": "O campo `dataHoraEvento` deve seguir o formato dd-MM-yyyy HH:mm:ss.",
     "path": "/agendamento"
   }
   ```

---

## Tratamento de Erros

- **400 Bad Request**  
  - Quando algum campo obrigatório estiver ausente, nulo ou em formato inválido (ex.: data/hora fora do padrão esperado).  
- **404 Not Found**  
  - Quando a notificação com o ID fornecido não existir no banco de dados (busca ou cancelamento).  
- **500 Internal Server Error**  
  - Para falhas inesperadas no servidor ou erros em tempo de execução.  

O projeto utiliza um _exception handler_ global (`@ControllerAdvice`) para mapear exceções em respostas padronizadas com timestamp, status HTTP, mensagem de erro e caminho.

---

## Testes

Para executar a suíte de testes unitários e de integração, utilize o comando Maven:

```bash
mvn test
```

- Os testes cobrem cenários como:  
  - Validação de payloads (dados obrigatórios, formato de data/hora).  
  - Fluxo de cadastro, busca e cancelamento no repositório (utilizando H2 em memória).  
  - Lógica do scheduler que dispara notificações — **mockando** os serviços de envio de e-mail e SMS.  

Certifique‐se de que o Docker Compose esteja parado (evite conflito de porta H2) antes de rodar testes que utilizam banco em memória.
