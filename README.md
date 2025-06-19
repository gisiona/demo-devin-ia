# Financial Control API - Microservices em Kotlin

API para controle financeiro pessoal e gestão de investimentos desenvolvida em Kotlin utilizando arquitetura de microserviços, facilitando a declaração anual do imposto de renda.

## 🏗️ Arquitetura

Este projeto implementa uma arquitetura de microserviços resiliente, performática e de fácil manutenção, composta por:

### Microserviços

1. **User Service** (Porta 8081)
   - Gerenciamento de usuários
   - Autenticação JWT
   - Cadastro e perfil de usuários

2. **Transaction Service** (Porta 8082)
   - Gestão de transações financeiras
   - Categorização de gastos
   - Controle de contas

3. **Investment Service** (Porta 8083)
   - Gerenciamento de investimentos
   - Acompanhamento de rentabilidade
   - Categorização para IR

4. **API Gateway** (Porta 8080)
   - Ponto único de entrada
   - Roteamento de requisições
   - Agregação de respostas

### Banco de Dados

- **MySQL 8.0** com schema otimizado para controle financeiro
- Tabelas para usuários, transações, investimentos e declarações de IR
- Relacionamentos bem definidos e índices otimizados

## 🚀 Tecnologias Utilizadas

- **Kotlin** - Linguagem principal
- **Spring Boot 3.2.0** - Framework web
- **Spring Data JPA** - Persistência de dados
- **Spring Security** - Autenticação e autorização
- **JWT** - Tokens de autenticação
- **MySQL 8.0** - Banco de dados
- **Docker & Docker Compose** - Containerização
- **Gradle** - Gerenciamento de dependências
- **OpenAPI/Swagger** - Documentação da API

## 📋 Pré-requisitos

- Docker e Docker Compose
- JDK 17+ (para desenvolvimento local)
- Gradle 7+ (para desenvolvimento local)

## 🔧 Configuração e Execução

### Usando Docker (Recomendado)

1. Clone o repositório:
```bash
git clone https://github.com/gisiona/demo-devin-ia.git
cd demo-devin-ia
```

2. Execute com Docker Compose:
```bash
docker-compose up -d
```

3. Aguarde todos os serviços iniciarem (pode levar alguns minutos na primeira execução)

4. Acesse a documentação da API:
   - API Gateway: http://localhost:8080/swagger-ui.html
   - User Service: http://localhost:8081/swagger-ui.html
   - Transaction Service: http://localhost:8082/swagger-ui.html
   - Investment Service: http://localhost:8083/swagger-ui.html

### Desenvolvimento Local

Para cada microserviço, execute:

```bash
cd [nome-do-servico]
./gradlew bootRun
```

## 📊 Estrutura do Banco de Dados

### Principais Tabelas

- **users** - Dados dos usuários
- **accounts** - Contas bancárias e cartões
- **categories** - Categorias de transações
- **transactions** - Transações financeiras
- **investments** - Investimentos
- **investment_types** - Tipos de investimento
- **tax_declarations** - Declarações de IR

## 🔐 Autenticação

A API utiliza JWT (JSON Web Tokens) para autenticação:

1. Registre um usuário: `POST /api/v1/users/register`
2. Faça login: `POST /api/v1/users/login`
3. Use o token retornado no header: `Authorization: Bearer {token}`

## 📱 Integração com React Native

A API foi projetada para facilitar a integração com aplicações React Native:

- **RESTful** - Endpoints padronizados
- **JSON** - Formato de dados consistente
- **CORS** - Configurado para requisições cross-origin
- **Documentação OpenAPI** - Especificação completa da API
- **Códigos HTTP** - Respostas padronizadas

### Exemplo de Uso

```javascript
// Registro de usuário
const response = await fetch('http://localhost:8080/api/v1/users/register', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    email: 'usuario@email.com',
    password: 'senha123',
    firstName: 'João',
    lastName: 'Silva',
    cpf: '12345678901'
  })
});

// Login
const loginResponse = await fetch('http://localhost:8080/api/v1/users/login', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    email: 'usuario@email.com',
    password: 'senha123'
  })
});

const { token } = await loginResponse.json();

// Requisições autenticadas
const userProfile = await fetch('http://localhost:8080/api/v1/users/profile', {
  headers: {
    'Authorization': `Bearer ${token}`
  }
});
```

## 🏥 Monitoramento

Cada microserviço expõe endpoints de health check:

- User Service: http://localhost:8081/actuator/health
- Transaction Service: http://localhost:8082/actuator/health
- Investment Service: http://localhost:8083/actuator/health
- API Gateway: http://localhost:8080/actuator/health

## 🧪 Testes

Execute os testes de cada microserviço:

```bash
cd [nome-do-servico]
./gradlew test
```

## 📈 Funcionalidades Principais

### Controle Financeiro
- ✅ Cadastro e gestão de usuários
- ✅ Gerenciamento de contas bancárias
- ✅ Categorização de transações
- ✅ Controle de receitas e despesas
- 🔄 Relatórios financeiros (em desenvolvimento)

### Gestão de Investimentos
- ✅ Cadastro de investimentos
- ✅ Categorização para IR
- ✅ Acompanhamento de rentabilidade
- 🔄 Integração com APIs de cotações (em desenvolvimento)

### Declaração de IR
- ✅ Estrutura para declarações anuais
- 🔄 Geração automática de relatórios (em desenvolvimento)
- 🔄 Exportação para formatos compatíveis (em desenvolvimento)

## 🤝 Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## 📞 Suporte

Para dúvidas ou suporte, abra uma issue no GitHub ou entre em contato através do email do projeto.

---

**Desenvolvido com ❤️ usando Kotlin e Spring Boot**
