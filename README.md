# 🛒 Workshop Spring Boot JPA / Hibernate

API REST desenvolvida em **Java** utilizando **Spring Boot**, **JPA/Hibernate** e banco de dados **H2/MySQL**.  
Projeto construído durante o curso [Java COMPLETO - Nelio Alves](https://devsuperior.com.br), com implementação de CRUD, relacionamentos entre entidades, tratamento de exceções e arquitetura em camadas.

---

## 🚀 Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3.5.4**
- **Spring Data JPA / Hibernate**
- **Banco de dados**: H2 (perfil de teste) / MySQL (desenvolvimento)
- **Maven**
- **Postman** (testes)
- **Heroku** (deploy)

---

## 📂 Estrutura do Projeto

src/main/java
- ├── entities # Modelos e mapeamento JPA
- ├── repositories # Interfaces de acesso ao banco
- ├── services # Regras de negócio
- └── resources # Controladores REST

---

## 🗄️ Modelo de Domínio

- **User** ↔ **Order** (One-to-Many)
- **Order** ↔ **OrderItem** (One-to-Many com atributos extras)
- **Order** ↔ **Payment** (One-to-One)
- **Product** ↔ **Category** (Many-to-Many)

---

## 🔧 Funcionalidades

- Criar, listar, atualizar e deletar usuários
- Registrar pedidos com status
- Relacionar produtos e categorias
- Calcular subtotal e total de pedidos
- Tratamento global de exceções

---

## 🛠️ Configuração

### Perfil de Teste (H2)
```properties```
- spring.datasource.driverClassName=org.h2.Driver
- spring.datasource.url=jdbc:h2:mem:testdb
- spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
- Perfil de Desenvolvimento (MySQL)

```properties```: 
- spring.datasource.url=jdbc:mysql://localhost:3306/seu_banco
- spring.datasource.username=root
- spring.datasource.password=senha
- spring.jpa.hibernate.ddl-auto=update

▶️ Executando o Projeto

# Clonar repositório
git clone https://github.com/marciocleydev/workshop_springBoot_jpa_2.0.git

# Entrar no diretório
cd workshop_springBoot_jpa_2.0

# Executar com Maven
./mvnw spring-boot:run
A aplicação estará disponível em:
➡️ http://localhost:8080

#📬 Endpoints Principais
Método	Endpoint	Descrição
- GET	/users	Lista todos usuários
- GET	/users/{id}	Busca por ID
- POST	/users	Cadastra novo usuário
- PUT	/users/{id}	Atualiza usuário
- DELETE	/users/{id}	Remove usuário
# 🛒 Workshop Spring Boot JPA / Hibernate

