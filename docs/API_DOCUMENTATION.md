# Documentação da API - Financial Control

## Visão Geral

A API Financial Control é uma solução completa para gerenciamento financeiro pessoal e controle de investimentos, desenvolvida em Kotlin com arquitetura de microserviços.

## Base URLs

- **Produção**: `https://api.financialcontrol.com`
- **Desenvolvimento**: `http://localhost:8080`

## Autenticação

A API utiliza JWT (JSON Web Tokens) para autenticação. Após o login, inclua o token no header de todas as requisições:

```
Authorization: Bearer {seu_jwt_token}
```

## Endpoints Principais

### 🔐 Autenticação

#### Registrar Usuário
```http
POST /api/v1/users/register
```

**Body:**
```json
{
  "email": "usuario@email.com",
  "password": "senha123",
  "firstName": "João",
  "lastName": "Silva",
  "cpf": "12345678901",
  "phone": "11999999999"
}
```

**Resposta (201):**
```json
{
  "id": 1,
  "email": "usuario@email.com",
  "firstName": "João",
  "lastName": "Silva",
  "fullName": "João Silva",
  "cpf": "12345678901",
  "phone": "11999999999",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00",
  "active": true
}
```

#### Login
```http
POST /api/v1/users/login
```

**Body:**
```json
{
  "email": "usuario@email.com",
  "password": "senha123"
}
```

**Resposta (200):**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "user": {
    "id": 1,
    "email": "usuario@email.com",
    "firstName": "João",
    "lastName": "Silva",
    "fullName": "João Silva",
    "cpf": "12345678901",
    "phone": "11999999999",
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00",
    "active": true
  }
}
```

### 👤 Usuários

#### Obter Perfil
```http
GET /api/v1/users/profile
Authorization: Bearer {token}
```

#### Atualizar Usuário
```http
PUT /api/v1/users/{id}
Authorization: Bearer {token}
```

**Body:**
```json
{
  "firstName": "João",
  "lastName": "Silva Santos",
  "phone": "11888888888"
}
```

#### Alterar Senha
```http
PUT /api/v1/users/{id}/password
Authorization: Bearer {token}
```

**Body:**
```json
{
  "currentPassword": "senhaAtual",
  "newPassword": "novaSenha123"
}
```

### 💰 Transações

#### Listar Transações
```http
GET /api/v1/transactions
Authorization: Bearer {token}
```

**Parâmetros de Query:**
- `page` (int): Página (padrão: 0)
- `size` (int): Tamanho da página (padrão: 20)
- `startDate` (date): Data inicial (formato: YYYY-MM-DD)
- `endDate` (date): Data final (formato: YYYY-MM-DD)
- `categoryId` (long): ID da categoria
- `type` (string): Tipo da transação (INCOME, EXPENSE, TRANSFER)

#### Criar Transação
```http
POST /api/v1/transactions
Authorization: Bearer {token}
```

**Body:**
```json
{
  "accountId": 1,
  "categoryId": 2,
  "amount": 150.50,
  "type": "EXPENSE",
  "description": "Almoço no restaurante",
  "transactionDate": "2024-01-15",
  "tags": ["alimentação", "trabalho"]
}
```

## Códigos de Status HTTP

| Código | Descrição |
|--------|-----------|
| 200 | Sucesso |
| 201 | Criado com sucesso |
| 400 | Requisição inválida |
| 401 | Não autorizado |
| 403 | Acesso negado |
| 404 | Recurso não encontrado |
| 409 | Conflito (ex: email já existe) |
| 422 | Entidade não processável |
| 500 | Erro interno do servidor |

## Integração com React Native

```javascript
import AsyncStorage from '@react-native-async-storage/async-storage';

class FinancialAPI {
  constructor(baseURL = 'http://localhost:8080') {
    this.baseURL = baseURL;
  }

  async getAuthToken() {
    return await AsyncStorage.getItem('auth_token');
  }

  async request(endpoint, options = {}) {
    const token = await this.getAuthToken();
    
    const config = {
      headers: {
        'Content-Type': 'application/json',
        ...(token && { Authorization: `Bearer ${token}` }),
        ...options.headers,
      },
      ...options,
    };

    const response = await fetch(`${this.baseURL}${endpoint}`, config);
    
    if (!response.ok) {
      throw new Error(`HTTP ${response.status}: ${response.statusText}`);
    }
    
    return response.json();
  }

  async login(email, password) {
    const response = await this.request('/api/v1/users/login', {
      method: 'POST',
      body: JSON.stringify({ email, password }),
    });
    
    await AsyncStorage.setItem('auth_token', response.token);
    return response;
  }

  async getTransactions(filters = {}) {
    const queryParams = new URLSearchParams(filters).toString();
    return this.request(`/api/v1/transactions?${queryParams}`);
  }

  async createTransaction(transaction) {
    return this.request('/api/v1/transactions', {
      method: 'POST',
      body: JSON.stringify(transaction),
    });
  }
}

export default FinancialAPI;
```

## Suporte

Para dúvidas sobre a API:
- 📧 Email: api-support@financialcontrol.com
- 📖 Documentação: https://docs.financialcontrol.com
- 🐛 Issues: https://github.com/gisiona/demo-devin-ia/issues
