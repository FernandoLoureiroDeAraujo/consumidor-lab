#!/bin/bash

source vars.sh

# Configurações do Prometheus
PROMETHEUS_URL="${HOST}:9090"

# Consulta para obter o último horário de execução do scheduler
SCHEDULE_QUERY="scheduler_last_execution_time_gauge_seconds{traceId=\"$TRACE_ID\"}"
SCHEDULE_RESPONSE=$(curl -s -G --data-urlencode "query=$SCHEDULE_QUERY" "$PROMETHEUS_URL/api/v1/query")
SCHEDULE_TIMESTAMP=$(echo "$SCHEDULE_RESPONSE" | jq -r '.data.result[0].value[1]')

# Consulta para obter o último horário de execução do processor
PROCESSOR_QUERY="processor_last_execution_time_gauge_seconds{traceId=\"$TRACE_ID\"}"
PROCESSOR_RESPONSE=$(curl -s -G --data-urlencode "query=$PROCESSOR_QUERY" "$PROMETHEUS_URL/api/v1/query")
PROCESSOR_TIMESTAMP=$(echo "$PROCESSOR_RESPONSE" | jq -r '.data.result[0].value[1]')

# Verifica se os horários foram obtidos corretamente
if [ -z "$SCHEDULE_TIMESTAMP" ] || [ -z "$PROCESSOR_TIMESTAMP" ]; then
    echo "Erro: Não foi possível obter os horários de execução do scheduler ou processor."
    exit 1
fi

# Convertendo para um formato de data legível
START_DATETIME=$(date -d "@$SCHEDULE_TIMESTAMP" +'+%Y-%m-%dT%T.%3N')
END_DATETIME=$(date -d "@$PROCESSOR_TIMESTAMP" +'+%Y-%m-%dT%T.%3N')

# Calculando a duração em segundos com precisão de milissegundos
DURATION=$(echo "scale=6; ($PROCESSOR_TIMESTAMP - $SCHEDULE_TIMESTAMP)" | bc)

TPS=$(echo "scale=6; $SIZE / $DURATION" | bc)

export RESPONSE_TIME=$DURATION
export TPS

# Exibindo os resultados
echo "Data e horário de início: $START_DATETIME (GMT-3)"
echo "Data e horário de término: $END_DATETIME (GMT-3)"
echo "Duração da requisição: $DURATION segundos"
echo
# Exibe os resultados
echo "Transactions Per Second: $TPS"
echo