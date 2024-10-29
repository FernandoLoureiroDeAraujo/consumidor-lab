#!/bin/bash

# Transactions Per Second

source vars.sh

url="${HOST}:16686/api/traces/$TRACE_ID"

trace_json=$(curl -s "$url")

# Extrai o startTime do primeiro span e o startTime + duration do último span
first_start_time=$(echo "$trace_json" | jq '.data[0].spans | sort_by(.startTime) | .[0].startTime')
last_span=$(echo "$trace_json" | jq '.data[0].spans | sort_by(.startTime) | .[-1]')
last_start_time=$(echo "$last_span" | jq '.startTime')
last_duration=$(echo "$last_span" | jq '.duration')

# Calcula o tempo final do último span
last_end_time=$((last_start_time + last_duration))

# Calcula o número total de spans
total_spans=$(echo "$trace_json" | jq '.data[0].spans | length')

# Calcula a duração total do trace em microssegundos
trace_duration=$((last_end_time - first_start_time))

# Converte a duração total para segundos (1 microssegundo = 1e6 nanossegundos)
trace_duration_in_seconds=$(echo "scale=6; $trace_duration / 1000000" | bc)

# Calcula a média de spans por segundo
TPS=$(echo "scale=6; $total_spans / $trace_duration_in_seconds" | bc)

export TPS

# Exibe os resultados
echo "Transactions Per Second: $TPS"