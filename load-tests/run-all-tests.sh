#!/bin/bash

echo "=== Executando Testes de Carga com K6 ==="
echo "Aplicação deve estar rodando em http://localhost:8080"
echo ""

BASE_URL=${BASE_URL:-"http://localhost:8080"}
echo "URL Base: $BASE_URL"
echo ""

echo "Verificando se a aplicação está disponível..."
if curl -s "$BASE_URL/api/users" > /dev/null; then
    echo "✓ Aplicação está rodando"
else
    echo "✗ Aplicação não está disponível em $BASE_URL"
    echo "Execute: mvn spring-boot:run"
    exit 1
fi

echo ""
echo "=== 1. Teste Básico de Carga ==="
k6 run -e BASE_URL="$BASE_URL" load-tests/k6-scripts/basic-load-test.js

echo ""
echo "=== 2. Teste de Rate Limiting ==="
k6 run -e BASE_URL="$BASE_URL" load-tests/k6-scripts/rate-limit-test.js

echo ""
echo "=== 3. Teste de Jornada do Usuário ==="
k6 run -e BASE_URL="$BASE_URL" load-tests/k6-scripts/user-journey-test.js

echo ""
echo "=== 4. Teste de Stress (Opcional - Demora ~20min) ==="
read -p "Executar teste de stress? (y/N): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    k6 run -e BASE_URL="$BASE_URL" load-tests/k6-scripts/stress-test.js
else
    echo "Teste de stress pulado."
fi

echo ""
echo "=== Testes Concluídos ==="
echo "Verifique os logs da aplicação para análise detalhada do rate limiting."
