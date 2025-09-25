# 🛒 Workshop Spring Boot JPA / Hibernate

API REST desenvolvida em **Java** utilizando **Spring Boot**, **JPA/Hibernate** e banco de dados **H2/MySQL**.  
Projeto construído durante o curso [Java COMPLETO - Nelio Alves](https://devsuperior.com.br), com implementação de CRUD, relacionamentos entre entidades, tratamento de exceções e arquitetura em camadas.

---

## 🚀 Tecnologias Utilizadas

- **Java 17** (atualizado para melhor compatibilidade)
- **Spring Boot 3.4.5**
- **Spring Data JPA / Hibernate**
- **Banco de dados**: H2 (perfil de teste) / MySQL (desenvolvimento)
- **Maven**
- **MapStruct** (mapeamento de DTOs)
- **HATEOAS** (REST maduro)
- **OpenAPI/Swagger** (documentação)
- **Flyway** (migração de banco)
- **TestContainers** (testes de integração)
- **Postman** (testes)
- **Heroku** (deploy)

---

## 📂 Estrutura do Projeto

```
src/main/java
├── config/          # Configurações do Spring
├── controllers/     # Controladores REST (API endpoints)
├── DTO/             # Data Transfer Objects
├── entities/        # Entidades JPA/Hibernate
├── exceptions/      # Tratamento de exceções personalizadas
├── file/            # Importação/exportação de arquivos
├── mapper/          # Mapeamento entre entidades e DTOs
├── repositories/    # Interfaces de acesso ao banco
├── serialization/   # Configurações de serialização
└── services/        # Regras de negócio
```

---

## 🗄️ Modelo de Domínio

- **User** ↔ **Order** (One-to-Many)
- **Order** ↔ **OrderItem** (One-to-Many com atributos extras)
- **Order** ↔ **Payment** (One-to-One)
- **Product** ↔ **Category** (Many-to-Many)

---

## 🔧 Funcionalidades

### Operações CRUD de Produtos
- Criar, listar, atualizar e deletar produtos
- Busca por nome (case-insensitive com wildcards)
- Paginação e ordenação
- Desabilitação lógica (soft delete)

### Exportação de Dados
- Exportação individual de produtos (PDF)
- Exportação em lote (XLSX, CSV, PDF)
- Suporte a diferentes formatos via content negotiation

### Importação em Massa
- Upload de arquivos para criação em massa
- Suporte a múltiplos formatos de arquivo

### APIs RESTful com HATEOAS
- Links hipermídia para navegação
- Suporte a JSON, XML e YAML
- Documentação OpenAPI/Swagger

### Outras Funcionalidades do Sistema Original
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

## 📋 Documentação da API

Acesse a documentação interativa Swagger em:
➡️ http://localhost:8080/swagger-ui.html

---

## ✨ Melhorias Implementadas

### 🔧 Correções Críticas
- ✅ Corrigido typo "disabe" → "disable" no link HATEOAS
- ✅ Mensagens de erro consistentes nos exception handlers
- ✅ Compatibilidade com Java 17

### 🏗️ Arquitetura
- ✅ Constructor injection substituindo @Autowired (melhor prática Spring)
- ✅ Campos final para imutabilidade
- ✅ Constantes para valores hardcoded
- ✅ Validação de entrada nos métodos principais

### 📚 Código e Documentação
- ✅ Comentários padronizados em inglês
- ✅ Query JPQL otimizada para busca case-insensitive
- ✅ Imports desnecessários removidos
- ✅ README atualizado com informações completas

---

## 📬 Endpoints Principais

### Produtos
| Método | Endpoint | Descrição | Parâmetros |
|--------|----------|-----------|------------|
| GET | `/products` | Lista todos produtos (paginado) | `page`, `size`, `direction` |
| GET | `/products/{id}` | Busca produto por ID | - |
| GET | `/products/name/{name}` | Busca produtos por nome | `page`, `size`, `direction` |
| POST | `/products` | Cadastra novo produto | Corpo: ProductDTO |
| PUT | `/products/{id}` | Atualiza produto | Corpo: ProductDTO |
| PATCH | `/products/{id}/disable` | Desabilita produto | - |
| DELETE | `/products/{id}` | Remove produto | - |
| GET | `/products/exportPage` | Exporta página de produtos | `page`, `size`, `direction`, Accept header |
| GET | `/products/export/{id}` | Exporta produto específico | Accept header |
| POST | `/products/massCreation` | Importação em massa | Arquivo multipart |

### Usuários (endpoints originais)
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/users` | Lista todos usuários |
| GET | `/users/{id}` | Busca por ID |
| POST | `/users` | Cadastra novo usuário |
| PUT | `/users/{id}` | Atualiza usuário |
| DELETE | `/users/{id}` | Remove usuário |

