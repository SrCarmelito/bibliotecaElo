
# API para Gestão de Empréstimos de Livros

Stack Utilizada para construir o Projeto:
- Java 17
- Liquibase
- SpringBoot
- PostgreSql
- SpringSecurity
- Envers Para Auditoria de Entidades

Para executar o projeto, após baixar o repositório local, faça uso dos perfis disponibilizadas 'biblioteca-h2[test]' para execução com banco de dados apenas em memória ou 'biblioteca-dev' se desejar verificar a iteração real com o banco de dados.

Ao Subir a API já serão inseridos alguns dados no banco para realizar alguns testes de Usuarios, Livros e Empréstimos;

'biblioteca-dev' -> como o liquibase não cria o banco de dados automaticamente, para isso seria necessário uma implementação via código para ser criado o banco de dados, o que tornaria moroso o desenvolvimento desta api para um resultado final pequeno! pois para solucionar, bastaria um script de criação de banco de dados, sendo assim, as configurações estão para criar apenas o schema diretamente no banco de dados padrão do PostgreSql 'postgres' como o nome do schema de 'biblioteca' ficando ali persistidas as tabelas e dados manipulados através da api.

# Documentação da API
As requisições requerem autenticação via Bearer Token JWT sendo que apenas os end-points abaixo estão configurados para não requerer tal autenticação:

/api/usuarios/login

/api/usuarios/reset-password

/api/usuarios/confirm-reset-password

/h2

Antes de realizar as requisições, necessário fazer um POST no end-point /api/usuarios/login para gerar um Bearer Token válido, conforme exemplo de Payload abaixo com usuário e senha ativo e válido já inserido no banco de dados:

```
{
	"login": "system",
	"senha": "123"
}
```
**O Token Gerado tem validade de 2(duas) horas.**

# Usuarios

```http
  POST /api/usuarios/login
```
#### Finalidade:
Gera o Token de autenticação do usuário

##### Request body:
| Parâmetro   | Tipo       | Descrição                           |
| :---------- | :--------- | :---------------------------------- |
| `login` | `string` | **Obrigatório**. login do usuário |
| `senha` | `string` | **Obrigatório**. senha do usuário |

**Comentário**: necessita que o usuário esteja com o status 'ATIVO'

#### Exemplo de Paylod 
```
{
	"login": "system",
	"senha": "123"
}
```
#### Exemplo de Retorno:
```
eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzeXN0ZW0iLCJpZCI6ImVlNGFlODgwLWE0ZGItNDU2My1iMzMwLTdlMmEyN2QyNjExNSIsImV4cCI6MTczMTg1MjkwNn0.J4f7nDJ8j3d4GyncR0LAlqKoCEMjV3We08eUAokPafE
```

```http
  POST /api/usuarios/novo-usuario
```
#### Finalidade:
Criar um novo Usuário

##### Request body:
| Parâmetro   | Tipo       | Descrição                           |
| :---------- | :--------- | :---------------------------------- |
| `nome` | `string` | **Obrigatório**. Entre 6 e 150 caracteres |
| `email` | `string` | **Obrigatório**. e-mail válido |
| `dataCadastro` | `data` | **Obrigatório**. formato: "yyyy-MM-dd" e não pode  ser maior que o dia atual |
| `telefone` | `string` | **Obrigatório**. entre 10 e 11 caracteres |
| `login` | `string` | **Obrigatório**. Entre 6 e 150 caracteres |
| `senha` | `string` | **Obrigatório**. Entre 6 e 150 caracteres, contendo 1 letra maiúscula, 1 minúscula e 1 número |
| `senhaConfirmacao` | `string` | **Obrigatório**. Entre 6 e 150 caracteres, contendo 1 letra maiúscula, 1 minúscula e 1 número e precisa ser igual ao atributo senha |
| `situacao` | `string` | Enum com o seguintes valores possíveis 'ATIVO' ou 'INATIVO' |

**Comentário**:
Por padrão o usuário é criado com o status de 'INATIVO', com a idéia de que algum administrador precise ativar posteriormente realiando um PUT neste mesmo end-point alterando o status para 'ativo';

#### Exemplo de Paylod 
```
{
  "nome": "Nome Utilizado Como Exemplo",
  "email": "meu.email@gmail.com.br",
  "dataCadastro": "2020-01-01",
  "telefone": "4589651265",
  "login": "meulogin",
  "senha": "Meulogin123",
  "senhaConfirmacao": "Meulogin123"
}
```
#### Exemplo de Retorno:
```
{
  "id": "5bba3a82-a4dc-4a50-bdf6-6b29e1b43a48",
  "nome": "Nome Utilizado Como Exemplo",
  "email": "meu.email@gmail.com.br",
  "dataCadastro": "2020-01-01",
  "telefone": "4589651265",
  "login": "meulogin",
  "situacao": "INATIVO"
}
```

```http
  POST /api/usuarios/reset-password
```
#### Finalidade:
Se Corresponder a um e-mail válido no banco de dados é enviado um e-mail contendo um token de autenticação com validade de 5 minutos e um html amigável com instruções para a troca de senha do usuário por um link;

**Não Requer Autenticação via Token JWT**

##### Request body:
| Parâmetro   | Tipo       | Descrição                           |
| :---------- | :--------- | :---------------------------------- |
| `email` | `string` | **Obrigatório**. e-mail válido |

**Comentário**:
Necessita ser um e-mail já inserido no banco de dados;

#### Exemplo de Paylod 
```
{
  "email": "carmelito.benali@gmail.com"
}
```
#### Exemplo de Retorno:
```
{
  "email": "carmelito.benali@gmail.com"
}
```
```http
  POST /api/usuarios/confirm-reset-password
```
#### Finalidade:
Após Envio por e-mail do token gerado pelo POST em /api/usuarios/reset-password o portador do e-mail acessa o link enviado no e-mail contendo token e então confirma a alteração de sua senha;

**Não Requer Autenticação via Token JWT**

##### Request body:
| Parâmetro   | Tipo       | Descrição                           |
| :---------- | :--------- | :---------------------------------- |
| `senha` | `string` | **Obrigatório**. Entre 6 e 150 caracteres, contendo 1 letra maiúscula, 1 minúscula e 1 número |
| `senhaConfirmacao` | `string` | **Obrigatório**. Entre 6 e 150 caracteres, contendo 1 letra maiúscula, 1 minúscula e 1 número e precisa ser igual ao atributo senha |
| `token` | `string` | **Obrigatório**. Token JWT autêntico e válido |

#### Exemplo de Paylod 
```
{
  "senha": "Carmelito123",
  "senhaConfirmacao": "Carmelito123",
  "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzeXN0ZW0iLCJpZCI6ImVlNGFlODgwLWE0ZGItNDU2My1iMzMwLTdlMmEyN2QyNjExNSIsImV4cCI6MTczMTg0ODU5MX0.50g1uMlIYULfeP156_tOlJ843mJ8OnSDzURZEbWM-xI"
}
```
#### Exemplo de Retorno:
```
```
```http
  PUT /api/usuarios
```
#### Finalidade:
Atualizar dados do Usuário

##### Request body:
| Parâmetro   | Tipo       | Descrição                           |
| :---------- | :--------- | :---------------------------------- |
| `id` | `uuid` | **Obrigatório**. id do usuário a ser atualizado, obrigatório conter na base de dados |
| `nome` | `string` | **Obrigatório**. Entre 6 e 150 caracteres |
| `email` | `string` | **Obrigatório**. e-mail válido |
| `dataCadastro` | `data` | **Obrigatório**. formato: "yyyy-MM-dd" e não pode  ser maior que o dia atual |
| `telefone` | `string` | **Obrigatório**. entre 10 e 11 caracteres |
| `login` | `string` | **Obrigatório**. Entre 6 e 150 caracteres |
| `senha` | `string` | **Obrigatório**. Entre 6 e 150 caracteres, contendo 1 letra maiúscula, 1 minúscula e 1 número |
| `senhaConfirmacao` | `string` | **Obrigatório**. Entre 6 e 150 caracteres, contendo 1 letra maiúscula, 1 minúscula e 1 número e precisa ser igual ao atributo senha |
| `situacao` | `string` | Enum com o seguintes valores possíveis 'ATIVO' ou 'INATIVO' |

#### Exemplo de Paylod 
```
{
  "id": "1e2bbebb-299b-4e9f-bf35-56b4da727f17",
  "nome": "Nome Para Ser Atualizado",
  "email": "meu.email@gmail.com.br",
  "dataCadastro": "2020-01-01",
  "telefone": "4589651265",
  "login": "meuLoginAtualizado",
  "senha": "Meulogin123",
  "senhaConfirmacao": "Meulogin123",
  "situacao": "ATIVO"
}
```
#### Exemplo de Retorno:
```
{
  "id": "1e2bbebb-299b-4e9f-bf35-56b4da727f17",
  "nome": "Nome Utilizado Como Exemplo",
  "email": "meu.email@gmail.com.br",
  "dataCadastro": "2020-01-01",
  "telefone": "4589651265",
  "login": "meuLoginAtualizado",
  "senha": "Meulogin123",
  "senhaConfirmacao": "Meulogin123",
  "situacao": "ATIVO"
}
```
```http
  GET /api/usuarios/{usuarioId}
```
#### Finalidade:
Listar dados de um Usuário específico

| Variável Path   | Tipo       | Descrição                           |
| :---------- | :--------- | :---------------------------------- |
| `id` | `uuid` | **Obrigatório**. id do usuário a ser buscado, obrigatório conter na base de dados |

#### Exemplo de Retorno:
```
{
  "id": "ee4ae880-a4db-4563-b330-7e2a27d26115",
  "nome": "Usuario Buscado Corretamente no Banco",
  "email": "usuario.buscado@gmail.com",
  "dataCadastro": "2024-11-13",
  "telefone": "4412345677",
  "login": "system",
  "situacao": "ATIVO"
}
```
```http
  GET /api/usuarios
```
#### Finalidade:
Lista Paginada dos usuários Cadastrados

#### Exemplo de Retorno:
```
{
  "content": [
    {
      "id": "ee4ae880-a4db-4563-b330-7e2a27d26115",
      "nome": "Usuario Buscado Corretamente no Banco",
      "email": "usuario.buscado@gmail.com",
      "dataCadastro": "2024-11-13",
      "telefone": "4412345677",
      "login": "system",
      "situacao": "ATIVO"
    },
    {
      "id": "5bc26f63-fc13-4e4f-8fc3-524b223a7d34",
      "nome": "Segundo Usuario Buscado Corretamente no Banco",
      "email": "segundo.usuario@gmail.com",
      "dataCadastro": "2020-10-11",
      "telefone": "1234567891",
      "login": "system",
      "situacao": "INATIVO"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "last": true,
  "totalPages": 1,
  "totalElements": 2,
  "size": 20,
  "number": 0,
  "sort": {
    "empty": true,
    "sorted": false,
    "unsorted": true
  },
  "numberOfElements": 2,
  "first": true,
  "empty": false
}
```
```http
  DELETE /api/usuarios/{usuarioId}
```
#### Finalidade:
Deletar um Usuário específico

| Variável Path   | Tipo       | Descrição                           |
| :---------- | :--------- | :---------------------------------- |
| `id` | `uuid` | **Obrigatório**. id do usuário a ser deletado, obrigatório conter na base de dados |

**Comentário**:
Caso Haja vinculo com outro registro não será permitido a exclusão;

#### Exemplo de Retorno:
```
```
# Livros
```http
  POST /api/livros
```
#### Finalidade:
Criar um novo Livro

##### Request body:
| Parâmetro   | Tipo       | Descrição                           |
| :---------- | :--------- | :---------------------------------- |
| `titulo` | `string` | **Obrigatório**. máximo de 1000 caracteres não aceitando repetição |
| `autor` | `string` | **Obrigatório**. máximo de 1000 caracteres não aceitando repetição|
| `isbn` | `long` | **Obrigatório**. |
| `dataPublicacao` | `data` | **Obrigatório**. formato: "yyyy-MM-dd"|
| `categoria` | `string` | Enum com o seguintes valores possíveis na tabela abaixo: |

**Enum categoria**
```
ACAO_AVENTURA, ARTE_FOTOGRAFIA, AUTOAJUDA, BIOGRAFIA, CONTO, CRIMES_REAIS, DISTOPIA,
 ENSAIOS, FANTASIA, FICCAO_CIENTIFICA, FICCAO_CONTEMPORANEA, FICCAO_FEMININA,
FICCAO_HISTORICA, FICCAO_POLICIAL, GASTRONOMIA, GRAPHIC_NOVEL, GUIAS_COMO_FAZER,
HISTORIA, HORROR, HUMANIDADES_CIENCIAS_SOCIAIS, HUMOR, INFANTIL, JOVEM_ADULTO, LGBTQ,
 MEMORIAS_AUTOBIOGRAFIA, NOVELA, NOVO_ADULTO, PATERNIDADE_FAMILIA, REALISMO_MAGICO,
RELIGIAO_ESPIRITUALIDADE, ROMANCE, TECNOLOGIA_CIENCIA, THRILLER_SUSPENSE, VIAGEM
```
#### Exemplo de Paylod 
```
{
  "titulo": "Titulo do Livro Cadastrado",
  "autor": "autor do Livro",
  "isbn": 7874521,
  "dataPublicacao": "1453-05-05",
  "categoria": "ROMANCE"
}
```
#### Exemplo de Retorno:
```
{
  "id": "6344bf34-b90e-4400-8bcf-8f8419cd5d88",
  "titulo": "Titulo do Livro Cadastrado",
  "autor": "autor do Livro",
  "isbn": 7874521,
  "dataPublicacao": "1453-05-05",
  "categoria": "ROMANCE"
}
```
```http
  GET /api/livros/{livroId}
```
#### Finalidade:
Listar dados de um Livro específico

| Variável Path   | Tipo       | Descrição                           |
| :---------- | :--------- | :---------------------------------- |
| `id` | `uuid` | **Obrigatório**. id do livro a ser buscado, obrigatório conter na base de dados |

#### Exemplo de Retorno:
```
{
  "id": "6344bf34-b90e-4400-8bcf-8f8419cd5d88",
  "titulo": "Titulo do Livro Buscado",
  "autor": "autor do Livro",
  "isbn": 7874521,
  "dataPublicacao": "1453-05-04",
  "categoria": "ROMANCE"
}
```
```http
  GET /api/livros
```
#### Finalidade:
Lista Paginada dos Livros Cadastrados

#### Exemplo de Retorno:
```
{
  "content": [
    {
      "id": "5eb157e9-1f44-4fd5-863e-f399550bf596",
      "titulo": "Dom Casmurro",
      "autor": "Machado de Assis",
      "isbn": 4023901803583,
      "dataPublicacao": "2007-01-18",
      "categoria": "FICCAO_CIENTIFICA"
    },
    {
      "id": "105c4e94-5e4d-4e16-977d-bbadfad684bb",
      "titulo": "Capitães de Areia",
      "autor": "Jorge Amado",
      "isbn": 6373135220044,
      "dataPublicacao": "1956-12-05",
      "categoria": "FICCAO_CONTEMPORANEA"
    },
    {
      "id": "5ddf0ef9-8759-4ca3-bb69-bc82679125ab",
      "titulo": "Vidas secas",
      "autor": "Graciliano ramos",
      "isbn": 394373427553,
      "dataPublicacao": "2006-03-01",
      "categoria": "FICCAO_POLICIAL"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "last": false,
  "totalPages": 1,
  "totalElements": 3,
  "size": 20,
  "number": 0,
  "sort": {
    "empty": true,
    "sorted": false,
    "unsorted": true
  },
  "numberOfElements": 20,
  "first": true,
  "empty": false
}
```
```http
  PUT /api/livros
```
#### Finalidade:
Atualizar dados de um Livro específico

##### Request body:
| Parâmetro   | Tipo       | Descrição                           |
| :---------- | :--------- | :---------------------------------- |
| `id` | `uuid` | **Obrigatório**. id do livro a ser atualizado, obrigatório conter na base de dados |
| `titulo` | `string` | **Obrigatório**. máximo de 1000 caracteres não aceitando repetição |
| `autor` | `string` | **Obrigatório**. máximo de 1000 caracteres não aceitando repetição|
| `isbn` | `long` | **Obrigatório**. |
| `dataPublicacao` | `data` | **Obrigatório**. formato: "yyyy-MM-dd"|
| `categoria` | `string` | Enum com o seguintes valores possíveis na tabela abaixo: |

**Enum categoria**
```
ACAO_AVENTURA, ARTE_FOTOGRAFIA, AUTOAJUDA, BIOGRAFIA, CONTO, CRIMES_REAIS, DISTOPIA,
 ENSAIOS, FANTASIA, FICCAO_CIENTIFICA, FICCAO_CONTEMPORANEA, FICCAO_FEMININA,
FICCAO_HISTORICA, FICCAO_POLICIAL, GASTRONOMIA, GRAPHIC_NOVEL, GUIAS_COMO_FAZER,
HISTORIA, HORROR, HUMANIDADES_CIENCIAS_SOCIAIS, HUMOR, INFANTIL, JOVEM_ADULTO, LGBTQ,
 MEMORIAS_AUTOBIOGRAFIA, NOVELA, NOVO_ADULTO, PATERNIDADE_FAMILIA, REALISMO_MAGICO,
RELIGIAO_ESPIRITUALIDADE, ROMANCE, TECNOLOGIA_CIENCIA, THRILLER_SUSPENSE, VIAGEM
```
#### Exemplo de Paylod 
```
{
  "id": "60451631-af32-4cfa-abca-ec3434694edb,
  "titulo": "Titulo do Livro a ser Atualizado",
  "autor": "autor Atualizado",
  "isbn": 153287465,
  "dataPublicacao": "1416-05-05",
  "categoria": "ROMANCE"
}
```
#### Exemplo de Retorno:
```
{
  "id": "60451631-af32-4cfa-abca-ec3434694edb,
  "titulo": "Titulo do Livro a ser Atualizado",
  "autor": "autor Atualizado",
  "isbn": 153287465,
  "dataPublicacao": "1416-05-05",
  "categoria": "ROMANCE"
}
```
```http
  DELETE /api/livros/{livroId}
```
#### Finalidade:
Deletar um Livro específico

| Variável Path   | Tipo       | Descrição                           |
| :---------- | :--------- | :---------------------------------- |
| `id` | `uuid` | **Obrigatório**. id do livro a ser deletado, obrigatório conter na base de dados |

**Comentário**:
Caso Haja vinculo com outro registro não será permitido a exclusão;

#### Exemplo de Retorno:
```
```
# Emprestimos
```http
  POST /api/emprestimos
```
#### Finalidade:
Criar um novo Emprestimo

##### Request body:
| Parâmetro   | Tipo       | Descrição                           |
| :---------- | :--------- | :---------------------------------- |
| `usuario` | `usuario` | **Obrigatório**. necessário informar apenas um id válido de um usuário já cadastrado no banco de dados |
| `livro` | `livro` | **Obrigatório**. necessário informar apenas um id válido de um livro já cadastrado no banco de dados |
| `dataEmprestimo` | `data` | **Obrigatório**. formato: "yyyy-MM-dd" não pode ser maior que o dia atual|
| `dataDevolucao` | `data` | **Obrigatório**. formato: "yyyy-MM-dd" não pode ser menor do que a dataEmprestimo|
| `status` | `string` | Enum com o seguintes valores possíveis 'AGUARDANDO_DEVOLUCAO' ou 'CONCLUIDO' |

#### Exemplo de Paylod 
```
{
  "usuario": {
    "id": "ee4ae880-a4db-4563-b330-7e2a27d26115"
  },
  "livro": {
    "id": "abdb306b-d91b-4d3e-bbf5-25c9b9234613"
  },
  "dataEmprestimo": "2024-11-17",
  "dataDevolucao": "2027-11-22",
  "status": "AGUARDANDO_DEVOLUCAO"
}
```
#### Exemplo de Retorno:
```
{
  "id": "7a3dad97-ba17-40e0-b5d9-d290d1f51f97",
  "usuario": {
    "id": "ee4ae880-a4db-4563-b330-7e2a27d26115",
    "nome": "Nome do Usuario que Emprestou o Livro",
    "email": "usuario.emprestimo@gmail.com",
    "dataCadastro": "2024-11-13",
    "telefone": "33225568741",
    "login": "system",
    "situacao": "ATIVO"
  },
  "livro": {
    "id": "abdb306b-d91b-4d3e-bbf5-25c9b9234613",
    "titulo": "O Meu Pé de Laranja Lima",
    "autor": "José Mauro de Vasconcelos",
    "isbn": 5479069103340,
    "dataPublicacao": "1991-11-16",
    "categoria": "PATERNIDADE_FAMILIA"
  },
  "dataEmprestimo": "2024-11-17",
  "dataDevolucao": "2027-11-22",
  "status": "AGUARDANDO_DEVOLUCAO"
}
```

```http
  PUT /api/emprestimos
```
#### Finalidade:
Atualizar dados de um Emprestimo já Realizado

##### Request body:
| Parâmetro   | Tipo       | Descrição                           |
| :---------- | :--------- | :---------------------------------- |
| `id` | `uuid` | **Obrigatório**. id do emprestimo a ser atualizado, obrigatório conter na base de dados |
| `dataDevolucao` | `data` | **Obrigatório**. formato: "yyyy-MM-dd" não pode ser menor do que a dataEmprestimo|
| `status` | `string` | Enum com o seguintes valores possíveis 'AGUARDANDO_DEVOLUCAO' ou 'CONCLUIDO'|

#### Exemplo de Paylod 
```
{
  "id": "8b052188-626c-47df-84cc-8eac444b210e",
  "dataDevolucao": "2025-11-22",
  "status": "CONCLUIDO"
}
```
#### Exemplo de Retorno:
```
{
  "id": "8b052188-626c-47df-84cc-8eac444b210e",
  "usuario": {
    "id": "ee4ae880-a4db-4563-b330-7e2a27d26115",
    "nome": "Nome do Usuario que Emprestou o Livro",
    "email": "usuario.emprestimo@gmail.com",
    "dataCadastro": "2024-11-13",
    "telefone": "33225568741",
    "login": "system",
    "situacao": "ATIVO"
  },
  "livro": {
    "id": "abdb306b-d91b-4d3e-bbf5-25c9b9234613",
    "titulo": "O Meu Pé de Laranja Lima",
    "autor": "José Mauro de Vasconcelos",
    "isbn": 5479069103340,
    "dataPublicacao": "1991-11-16",
    "categoria": "PATERNIDADE_FAMILIA"
  },
  "dataEmprestimo": "2024-11-17",
  "dataDevolucao": "2025-11-22",
  "status": "CONCLUIDO"
}
```
```http
  GET /api/emprestimos/{emprestimoId}
```
#### Finalidade:
Listar dados de um Emprestimo específico

| Variável Path   | Tipo       | Descrição                           |
| :---------- | :--------- | :---------------------------------- |
| `id` | `uuid` | **Obrigatório**. id do Empréstimo a ser buscado, obrigatório conter na base de dados |

#### Exemplo de Retorno:
```
{
  "id": "8b052188-626c-47df-84cc-8eac444b210e",
  "usuario": {
    "id": "ee4ae880-a4db-4563-b330-7e2a27d26115",
    "nome": "Nome do Usuario que Emprestou o Livro",
    "email": "usuario.emprestimo@gmail.com",
    "dataCadastro": "2024-11-13",
    "telefone": "33225568741",
    "login": "system",
    "situacao": "ATIVO"
  },
  "livro": {
    "id": "abdb306b-d91b-4d3e-bbf5-25c9b9234613",
    "titulo": "O Meu Pé de Laranja Lima",
    "autor": "José Mauro de Vasconcelos",
    "isbn": 5479069103340,
    "dataPublicacao": "1991-11-16",
    "categoria": "PATERNIDADE_FAMILIA"
  },
  "dataEmprestimo": "2024-11-17",
  "dataDevolucao": "2025-11-22",
  "status": "CONCLUIDO"
}
```
```http
  GET /api/emprestimos
```
#### Finalidade:
Lista Paginada dos Emprestimos Cadastrados

#### Exemplo de Retorno:
```
{
  "content": [
    {
      "id": "726b1719-e942-4a79-9d1a-6fb5d4015b26",
      "usuario": {
        "id": "ee4ae880-a4db-4563-b330-7e2a27d26115",
        "nome": "Carmelito Junior Delcielo Benali",
        "email": "carmelito.benali@gmail.com",
        "dataCadastro": "2024-11-13",
        "telefone": "44988080437",
        "login": "system",
        "situacao": "ATIVO"
      },
      "livro": {
        "id": "5eb157e9-1f44-4fd5-863e-f399550bf596",
        "titulo": "Dom Casmurro",
        "autor": "Machado de Assis",
        "isbn": 4023901803583,
        "dataPublicacao": "2007-01-18",
        "categoria": "FICCAO_CIENTIFICA"
      },
      "dataEmprestimo": "2024-06-02",
      "dataDevolucao": "2024-07-02",
      "status": "CONCLUIDO"
    },
    {
      "id": "14dcbbd7-2257-4db7-be75-5b156d7ab07d",
      "usuario": {
        "id": "ee4ae880-a4db-4563-b330-7e2a27d26115",
        "nome": "Carmelito Junior Delcielo Benali",
        "email": "carmelito.benali@gmail.com",
        "dataCadastro": "2024-11-13",
        "telefone": "44988080437",
        "login": "system",
        "situacao": "ATIVO"
      },
      "livro": {
        "id": "105c4e94-5e4d-4e16-977d-bbadfad684bb",
        "titulo": "Capitães de Areia",
        "autor": "Jorge Amado",
        "isbn": 6373135220044,
        "dataPublicacao": "1956-12-05",
        "categoria": "FICCAO_CONTEMPORANEA"
      },
      "dataEmprestimo": "2024-04-03",
      "dataDevolucao": "2024-05-03",
      "status": "CONCLUIDO"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 2,
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "last": false,
  "totalElements": 2,
  "totalPages": 1,
  "numberOfElements": 2,
  "size": 20,
  "number": 0,
  "sort": {
    "empty": true,
    "sorted": false,
    "unsorted": true
  },
  "first": true,
  "empty": false
}
```
# Recomendações
```http
  GET /api/recomendacoes/{usuarioId}
```
#### Finalidade:
Listar recomendações de Livros de um usuário específico excluindo-se os livros já emprestados por ele

| Variável Path   | Tipo       | Descrição                           |
| :---------- | :--------- | :---------------------------------- |
| `id` | `uuid` | **Obrigatório**. id do usuario|

#### Exemplo de Retorno:
```
[
  {
    "id": "45dc2abb-db8e-401f-902d-6c4e6a0d4b5f",
    "titulo": "Anna Karênina",
    "autor": "Liev Tolstói",
    "isbn": 1798428492,
    "dataPublicacao": "1999-08-16",
    "categoria": "HORROR"
  },
  {
    "id": "25664968-ed1d-48b2-96a3-68828cd01a27",
    "titulo": "Ensaio Sobre a Cegueira",
    "autor": "José Saramago",
    "isbn": 9888317998228,
    "dataPublicacao": "1918-04-23",
    "categoria": "HORROR"
  },
  {
    "id": "d10759ea-39d9-430c-a2f7-3bc730fa6374",
    "titulo": "O apanhador no campo de centeio",
    "autor": "J.D. Salinger",
    "isbn": 8191368723398,
    "dataPublicacao": "1920-03-23",
    "categoria": "HORROR"
  },
]
```
