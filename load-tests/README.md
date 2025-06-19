# Load Testing com K6

Este diretório contém scripts de teste de carga usando K6 para validar o desempenho da API e o funcionamento do rate limiting.

## Pré-requisitos

1. Instalar K6:
```bash
# Ubuntu/Debian
sudo apt update
sudo apt install k6

# macOS
brew install k6

# Windows
choco install k6
```

2. Aplicação rodando localmente:
```bash
cd /home/ubuntu/demo-devin-ia
mvn spring-boot:run
```

## Scripts Disponíveis

### 1. Teste Básico de Carga (`basic-load-test.js`)
Testa o desempenho básico da API com carga gradual.

```bash
k6 run load-tests/k6-scripts/basic-load-test.js
```

**Configuração:**
- 30s: 0 → 10 usuários
- 1m: 10 → 20 usuários  
- 30s: 20 → 0 usuários

**Métricas:**
- 95% das requisições < 500ms
- Taxa de erro < 10%

### 2. Teste de Rate Limiting (`rate-limit-test.js`)
Valida se o rate limiting está funcionando corretamente.

```bash
k6 run load-tests/k6-scripts/rate-limit-test.js
```

**Objetivo:**
- Fazer 25 requisições rápidas de um único usuário
- Verificar se o rate limit é acionado (HTTP 429)
- Aguardar reset do rate limit

### 3. Teste de Stress (`stress-test.js`)
Testa o comportamento da API sob alta carga.

```bash
k6 run load-tests/k6-scripts/stress-test.js
```

**Configuração:**
- Escala gradual até 200 usuários simultâneos
- Duração total: 20 minutos
- Testa múltiplos endpoints

### 4. Teste de Jornada do Usuário (`user-journey-test.js`)
Simula fluxos completos de uso da API.

```bash
k6 run load-tests/k6-scripts/user-journey-test.js
```

**Fluxo:**
1. Listar usuários
2. Criar usuário
3. Buscar usuário criado
4. Atualizar usuário
5. Deletar usuário

## Configuração de Ambiente

Você pode configurar a URL base da API:

```bash
# Teste local
k6 run -e BASE_URL=http://localhost:8080 load-tests/k6-scripts/basic-load-test.js

# Teste em ambiente de staging
k6 run -e BASE_URL=https://api-staging.example.com load-tests/k6-scripts/basic-load-test.js
```

## Executando Todos os Testes

```bash
#!/bin/bash
echo "Executando testes de carga..."

echo "1. Teste básico de carga"
k6 run load-tests/k6-scripts/basic-load-test.js

echo "2. Teste de rate limiting"
k6 run load-tests/k6-scripts/rate-limit-test.js

echo "3. Teste de stress"
k6 run load-tests/k6-scripts/stress-test.js

echo "4. Teste de jornada do usuário"
k6 run load-tests/k6-scripts/user-journey-test.js

echo "Testes concluídos!"
```

## Interpretando Resultados

### Métricas Importantes

- **http_req_duration**: Tempo de resposta das requisições
- **http_req_rate**: Taxa de requisições por segundo
- **http_req_failed**: Porcentagem de requisições falhadas
- **vus**: Número de usuários virtuais ativos

### Rate Limiting

- **HTTP 429**: Rate limit acionado corretamente
- **X-Rate-Limit-Remaining**: Header com tokens restantes
- Logs estruturados em JSON para análise

### Exemplo de Saída

```
✓ status is 200
✓ response time < 500ms
✓ rate limit triggered

http_req_duration..........: avg=245ms min=89ms med=198ms max=1.2s p(90)=456ms p(95)=678ms
http_req_rate..............: 45.2/s
rate_limits................: 15.2% ✓ 152 ✗ 848
```

## Troubleshooting

### Rate Limiting Não Funciona
1. Verificar se a aplicação está rodando
2. Confirmar configuração em `application.yml`
3. Verificar logs da aplicação

### Performance Baixa
1. Verificar recursos do sistema (CPU, memória)
2. Analisar logs de erro
3. Ajustar configurações de rate limiting

### Falhas de Conexão
1. Confirmar URL base
2. Verificar firewall/proxy
3. Testar conectividade manual
