#!/bin/bash

source vars.sh

URL="${HOST}:16686/api/traces/$TRACE_ID"

JSON=$(curl -s "$URL")

# Extrai o startTime do primeiro e do último span
FIRST_START_TIME=$(echo "$JSON" | jq '.data[0].spans | sort_by(.startTime) | .[0].startTime')
LAST_START_TIME=$(echo "$JSON" | jq '.data[0].spans | sort_by(.startTime) | .[-1].startTime')

# Verifica se os tempos foram corretamente extraídos
if [ -z "$FIRST_START_TIME" ] || [ -z "$LAST_START_TIME" ]; then
  echo "Erro ao extrair os tempos do JSON"
  exit 1
fi

# Calcula a diferença entre o primeiro e o último startTime (em microssegundos)
DURATION=$((LAST_START_TIME - FIRST_START_TIME))

# Converte para segundos (1 microssegundo = 1e6 nanossegundos)
RESPONSE_TIME=$(echo "scale=6; $DURATION / 1000000" | bc)

export RESPONSE_TIME

# Exibe o tempo total do trace em segundos
echo "Response Time: ${RESPONSE_TIME}"