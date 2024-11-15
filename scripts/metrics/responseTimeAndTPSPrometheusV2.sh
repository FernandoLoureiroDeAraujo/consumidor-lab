#!/bin/bash

source vars.sh

# Convertendo para timestamps UNIX (em segundos)
START_TIMESTAMP=$(date -d "$START_TIME" +%s)
END_TIMESTAMP=$(date -d "$END_TIME" +%s)

# Calculando a duração em segundos
DURATION=$(($END_TIMESTAMP - $START_TIMESTAMP))

# Calculando Transactions Per Second (TPS)
TPS=$(echo "scale=6; $SIZE / $DURATION" | bc)

# Exibindo os resultados
echo "Data e horário de início: $START_TIME (UTC)"
echo "Data e horário de término: $END_TIME (UTC)"
echo "Duração da requisição: $DURATION segundos"
echo
# Exibe os resultados
echo "Transactions Per Second: $TPS"
echo