#!/bin/bash

START_TIME=$(date -u +"%Y-%m-%dT%H:%M:%SZ")

############################## VARIAVEIS
HOST="$1"
SIZE="$2"
PARAMS="$3"

############################## GERA TRACE PARENT
# see spec: https://www.w3.org/TR/trace-context
# version-format   = trace-id "-" parent-id "-" trace-flags
# trace-id         = 32HEXDIGLC  ; 16 bytes array identifier. All zeroes forbidden
# parent-id        = 16HEXDIGLC  ; 8 bytes array identifier. All zeroes forbidden
# trace-flags      = 2HEXDIGLC   ; 8 bit flags. Currently, only one bit is used. See below for detail

VERSION="00" # fixed in spec at 00
TRACE_ID="$(cat /dev/urandom | tr -dc 'a-f0-9' | fold -w 32 | head -n 1)"
PARENT_ID="00$(cat /dev/urandom | tr -dc 'a-f0-9' | fold -w 14 | head -n 1)"
TRACE_FLAG="01"   # sampled
TRACE_PARENT="$VERSION-$TRACE_ID-$PARENT_ID-$TRACE_FLAG"

############################## EXECUTA REQUEST
ENDPOINT="scheduler/send-data"
URL="${HOST}:8080/${ENDPOINT}?loop-size=${SIZE}&${PARAMS}"

echo "$URL"
echo "$TRACE_PARENT"

curl --location --request GET "${URL}" \
--header "traceparent: $TRACE_PARENT" \
--header "Content-Type: application/json"

############################## ESPERA CONCLUIR REQUEST

URL="${HOST}:16686/api/traces/$TRACE_ID"

while true; do

  JSON=$(curl -s "$URL")

  CURRENT_COUNT=$(echo "$JSON" | jq '[.data[]?.spans[]? | select(.operationName == "processor-queue send")] | length')

  echo "Ocorrências encontradas: $CURRENT_COUNT"

    if [ "$CURRENT_COUNT" -ge "$SIZE" ]; then
        echo "Alvo de $TARGET_COUNT ocorrências atingido."
        break
    else
        echo "Aguardando mais processamentos..."
        sleep 2
    fi
done

END_TIME=$(date -u +"%Y-%m-%dT%H:%M:%SZ")

############################## EXPORTA DATAS PARA CONSULTAR PELO PROMETHEUS
cat <<EOF > vars.sh
HOST='$HOST'
TRACE_ID='$TRACE_ID'
START_TIME='$START_TIME'
END_TIME='$END_TIME'
EOF