# Conecta Saúde Acessível - Backend API

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green)
![Spring Security](https://img.shields.io/badge/Spring%20Security-5.x-lightgrey)
![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-ORM-orange)
![MySQL](https://img.shields.io/badge/MySQL-Database-blue)
![Maven](https://img.shields.io/badge/Maven-Build-red)

---

## 🚀 Visão Geral do Projeto

Este repositório contém o **backend (API RESTful)** da plataforma **Conecta Saúde Acessível**, desenvolvida como projeto final de Desenvolvimento Full Stack.

O objetivo do projeto é facilitar o acesso de **Pessoas com Deficiência (PCD)** a profissionais e serviços de saúde que ofereçam atendimento inclusivo e adaptado, alinhando-se à **ODS 3 - Saúde e Bem-Estar** da ONU.

## ✨ Funcionalidades Principais (Backend)

* **Autenticação e Autorização:** Gerenciamento de usuários (`UsuarioPCD`, `ProfissionalDeSaude`, `Admin`) com Spring Security.
* **Gestão de Perfis de Profissionais de Saúde:** CRUD completo para cadastro e gerenciamento de profissionais, incluindo informações de acessibilidade do consultório e idiomas de comunicação (ex: Libras).
* **Gestão de Perfis de Usuários PCD:** CRUD para que usuários possam gerenciar seus dados e necessidades específicas.
* **Agendamento de Consultas:** Lógica para que usuários PCD agendem consultas com profissionais, com status de agendamento.
* **Persistência de Dados:** Utilização de MySQL como banco de dados, gerenciado via Spring Data JPA.

## 🛠️ Tecnologias Utilizadas (Backend)

* **Java 17+**
* **Spring Boot 3.x**
* **Spring Web (Spring MVC)**
* **Spring Data JPA**
* **Spring Security**
* **MySQL Driver**
* **Lombok** (para reduzir boilerplate code)
* **Maven** (Gerenciamento de dependências)

## 📦 Estrutura do Projeto

A estrutura do projeto segue as convenções do Spring Boot, organizada em pacotes por responsabilidade (controllers, services, repositories, models, config, etc.).

## ⚙️ Como Rodar o Projeto Localmente

### Pré-requisitos

* **Java Development Kit (JDK) 17 ou superior**
* **MySQL Server** (versão 8.0+ recomendada)
* **Maven** (geralmente incluído com o JDK ou seu IDE)
* Um **IDE** (Eclipse, IntelliJ IDEA, VS Code com extensões Java)

### Configuração do Banco de Dados

1.  Crie um banco de dados MySQL com o nome `conecta_saude_db`.
    ```sql
    CREATE DATABASE IF NOT EXISTS `conecta_saude_db`;
    USE `conecta_saude_db`;
    ```
2.  Execute os scripts SQL para criar as tabelas necessárias: `users`, `roles`, `user_roles`, `usuarios_pcd`, `profissionais_saude`, `agendamentos`.
    [**Link para os scripts SQL completos**](ADICIONAR_LINK_PARA_OS_SCRIPTS_SQL_AQUI)

### Configuração da Aplicação

1.  Clone este repositório:
    ```bash
    git clone [https://github.com/fisiofaz/conecta-saude-api](https://github.com/fisiofaz/conecta-saude-api.git)
    ```
2.  Navegue até o diretório do projeto:
    ```bash
    cd conecta-saude-api
    ```
3.  Abra o arquivo `src/main/resources/application.properties` e configure as credenciais do seu banco de dados MySQL:
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/conecta_saude_db?useSSL=false&serverTimezone=UTC
    spring.datasource.username=root
    spring.datasource.password=SUA_SENHA_MYSQL_AQUI
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.format_sql=true
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
    server.port=8080
    ```
    **ATENÇÃO:** Substitua `SUA_SENHA_MYSQL_AQUI` pela sua senha real do MySQL.

### Executando a Aplicação

Você pode rodar a aplicação Spring Boot de algumas maneiras:

* **Via IDE:** No Eclipse, localize o arquivo `ConectaSaudeApiApplication.java` (em `src/main/java/com/conecta_saude/conecta_saude_api`). Clique com o botão direito nele e selecione `Run As` > `Java Application`.
* **Via Maven (Terminal):** No diretório raiz do projeto, execute:
    ```bash
    mvn spring-boot:run
    ```

A API estará disponível em `http://localhost:8080`.

## ☁️ Deploy

### Backend (API)

* **Plataforma:** Render
* **Link do Deploy:** [ADICIONAR_LINK_DO_DEPLOY_DO_RENDER_AQUI]

### Banco de Dados

* **Plataforma:** Render
* **(Opcional):** Link do serviço de Banco de Dados se acessível externamente.

---

## 🤝 Contribuições

Este projeto é desenvolvido para fins educacionais. Contribuições são bem-vindas para melhorias e novas funcionalidades.

## 📄 Licença

[ESCOLHA UMA LICENÇA, ex: MIT License](https://opensource.org/licenses/MIT)

## 📞 Contato

[Fábio André Zatta] - [fisiofaz8@gmail.com] - [https://www.linkedin.com/in/fabiozatta-dweb/]