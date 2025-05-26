
# Sistema de Ponto

## Descrição
Este é o projeto **Sistema de Ponto**, uma aplicação de gerenciamento de ponto de funcionários, onde é possível registrar, editar e consultar informações sobre funcionários, turnos, justificativas e pontos.

## Tecnologias
- Java 17
- Spring Boot
- Spring Data JPA
- Thymeleaf
- MySQL

## Como rodar o projeto

1. Certifique-se de ter o Java 17 e o MySQL instalados em sua máquina.
2. Clone este repositório:
   ```
   git clone https://github.com/salomaotech/projeto-ponto.git
   ```
3. Navegue até o diretório do projeto:
   ```
   cd projeto-ponto
   ```
4. Crie o banco de dados `pontofacil_database` no MySQL.
5. Configure o banco de dados no arquivo `application.properties`:
   ```properties
   spring.application.name=projeto-ponto
   spring.datasource.url=jdbc:mysql://localhost:3307/pontofacil_database
   spring.datasource.username=root
   spring.datasource.password=123456
   spring.jpa.hibernate.ddl-auto=update
   ```
6. Execute o projeto usando o comando Maven:
   ```
   mvn spring-boot:run
   ```

## Endpoints

Os endpoints da aplicação estão organizados por módulos, e cada funcionalidade é acessada via uma URL específica conforme seu módulo correspondente.

Por exemplo:
- Funcionalidades relacionadas a funcionários são acessadas via caminhos que começam com `/funcionario/`.
- Para turnos, o caminho base é `/turno/`.
- Para usuários, `/usuario/`.
- E assim por diante para os demais módulos do sistema.

A estrutura genérica para acesso aos endpoints é:

```
http://localhost:8080/{modulo}/{recurso}
```

onde `{modulo}` representa o módulo desejado (como `funcionario`, `turno`, `usuario`) e `{recurso}` representa a operação ou entidade específica dentro daquele módulo.

## Regras de Funcionamento

1. Quando um funcionário é cadastrado, o nome dele aparece na tela de ponto, e o administrador pode bater o ponto. Também é possível pesquisar por período. Para bater o ponto, basta clicar em "editar" no funcionário.

2. Não é possível alterar o e-mail e a palavra-chave do cadastro do usuário. Apenas a senha pode ser alterada.

3. Quando a senha for alterada, o usuário será deslogado e precisará realizar login com a nova senha.

## Dependências

As principais dependências do projeto são:

- `spring-boot-starter-web`: Para configuração do servidor web.
- `spring-boot-starter-data-jpa`: Para integração com o banco de dados MySQL usando JPA.
- `spring-boot-starter-thymeleaf`: Para a renderização de páginas HTML.

## Contribuição

Se você deseja contribuir com o projeto, por favor, siga as etapas abaixo:

1. Faça um fork do repositório.
2. Crie uma nova branch para suas alterações (`git checkout -b minha-branch`).
3. Realize as alterações e faça um commit (`git commit -am 'Adiciona nova funcionalidade'`).
4. Envie para o repositório remoto (`git push origin minha-branch`).
5. Crie um Pull Request.

## Licença


# Rodando o projeto com o Docker

## Descrição
Este projeto é uma aplicação Spring Boot que inclui um banco de dados MySQL embutido em um container Docker.

## Atenção

- Dentro da pasta de ```Projeto/docker-projeto-ponto``` estão as configurações do Docker
- Depois que gerar a build com o nome ```ponto-0.0.1-SNAPSHOT.jar``` coloque o arquivo dentro da pasta ```Projeto/docker-projeto-ponto``` e gere a imagem do Docker a partir dela.

## Como usar

### 1. Pré-requisitos
- Docker instalado no seu computador.

### 2. Rodando o container
Para subir a aplicação e o banco juntos, rode o seguinte comando:

```
docker run -d -p 8080:8080 --name projeto-ponto-container projeto-ponto
```

Isso executa o container em background e mapeia a porta 8080 do container para a porta 8080 do host.

### 3. Acessando a aplicação
Abra o navegador e acesse:

```
http://localhost:8080/
```

### 4. Parar o container
Para parar o container que está rodando:

```
docker stop projeto-ponto-container
```

### 5. Remover o container
Para remover o container parado:

```
docker rm projeto-ponto-container
```

### 6. Remover a imagem
Para remover a imagem (certifique-se que o container esteja parado e removido):

```
docker rmi projeto-ponto
```

## Informações importantes
- O banco de dados MySQL está rodando dentro do container.
- A aplicação Spring Boot está configurada para se conectar a esse banco localmente dentro do container.
- A porta 3306 do banco **não precisa** ser exposta externamente.
- A aplicação está acessível pela porta 8080 no host.

---
