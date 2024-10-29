#!/bin/bash

source vars.sh

# Usa 1 dia para agregar em 1 unico bloco, mas o que vale é o start e end date
PROMETHEUS_URL="${HOST}:9090"
QUERY="avg_over_time(jvm_memory_used_bytes[1d]) / 1024 / 1024" # PEGA MEDIA E MOSTRA RESULTADO EM MB
STEP=1d

RESPONSE=$(curl -s -G \
  --data-urlencode "query=$QUERY" \
  --data-urlencode "start=$START_TIME" \
  --data-urlencode "end=$END_TIME" \
  --data-urlencode "step=$STEP" \
  "$PROMETHEUS_URL/api/v1/query_range" | jq -r '.data.result[0].values[-1][1]')

export MEMORY_USAGE=$RESPONSE

echo "Memory Usage: $RESPONSE" # Em MB
