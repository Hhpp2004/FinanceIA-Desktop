# BB - Gestão Financeira Desktop

Aplicação desktop para gestão financeira pessoal, construída com **JavaFX + Spring Boot + JPA + PostgreSQL**.

## Visão Geral

O projeto oferece fluxo completo de autenticação e gestão financeira:

- cadastro de usuário com confirmação por e-mail
- login com senha criptografada (BCrypt)
- dashboard com resumo financeiro (saldo, entradas e saídas)
- cadastro de transações (entrada/saída) com categoria
- extratos mensais com histórico de operações
- recuperação e redefinição de senha via código por e-mail
- configurações de conta (nome, telefone, senha e logout)

## Stack Tecnológica

- Java 17 (alvo do projeto)
- Spring Boot 3.2.3
- Spring Data JPA
- Spring Mail
- JavaFX 21.0.2
- PostgreSQL
- Lombok
- Maven Wrapper (`mvnw`)

## Arquitetura do Projeto

Estrutura principal em `src/main/java/financas/bb/com`:

- `View/`: telas JavaFX e navegação
- `Controller/`: regras de negócio
- `Models/`: entidades JPA (`User`, `Saldo`, `Extrato`, `Operacao`)
- `Repository/`: acesso a dados com Spring Data
- `Service/`: sessão, e-mail e eventos de encerramento
- `Security/`: utilitário de senha com BCrypt

Classes de entrada:

- `Launcher`: inicializa JavaFX
- `JavaFxSpringApp`: integra ciclo de vida JavaFX com contexto Spring
- `BBApplication`: bootstrap do Spring Boot

## Banco de Dados

Configuração em `src/main/resources/application.properties`.

- banco esperado: PostgreSQL local
- estratégia JPA: `spring.jpa.hibernate.ddl-auto=update`
- o schema é atualizado automaticamente conforme as entidades

## Pré-requisitos

- JDK **17**
- PostgreSQL em execução
- Maven (opcional, pois existe `./mvnw`)

## Configuração Local

1. Ajuste `src/main/resources/application.properties` com suas credenciais locais de banco e SMTP.
2. Crie o banco `financas` no PostgreSQL (ou altere a URL no arquivo).
3. Garanta acesso SMTP válido para envio de códigos de confirmação e recuperação.

## Execução

Opção recomendada (IDE):

1. Importar como projeto Maven.
2. Executar a classe principal `financas.bb.com.Launcher`.

Via terminal (build):

```bash
./mvnw clean package
```

## Testes

```bash
./mvnw test
```

## Análise Técnica (Estado Atual)

Durante a análise deste código, foram observados pontos importantes:

- o build falhou no ambiente com **JDK 25** (`TypeTag :: UNKNOWN`), indicando incompatibilidade de toolchain; use **JDK 17**
- existe duplicidade da dependência `spring-boot-starter-web` no `pom.xml`
- o `javafx-maven-plugin` está configurado com `mainClass` diferente do pacote atual (`com.financas.Launcher`)
- há credenciais sensíveis no `application.properties`; para subir no Git, mantenha apenas placeholders

## Melhorias Recomendadas Antes de Publicar

- remover segredos do repositório e usar variáveis de ambiente
- revisar `pom.xml` (dependências duplicadas e `mainClass` do JavaFX)
- adicionar testes de unidade para regras de saldo/extrato
- incluir pipeline CI para validar build com Java 17

## Licença

Defina a licença do projeto (ex.: MIT) antes da publicação final.