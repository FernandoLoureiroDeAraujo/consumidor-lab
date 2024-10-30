#!/bin/bash

# ENVS
HOST="$1"
EXECUTION_VOLUME="$2"
EXECUTION_REPEAT="$3"

# BROKERS + SERIALIZERS
BROKERS=("JMS" "KAFKA")
SERIALIZERS=("JSON" "PROTOBUF" "PROTOSTUFF" "KRYO" "MSGPACK")

for (( EXECUTION_NUMBER=1; EXECUTION_NUMBER<=EXECUTION_REPEAT; EXECUTION_NUMBER++ )); do
    for BROKER in "${BROKERS[@]}"; do
        for SERIALIZER in "${SERIALIZERS[@]}"; do

            echo "ID Execucao: $EXECUTION_NUMBER"

            # EXECUTE REQUEST
            PARAMS="message-broker-type=$BROKER&message-format-type=$SERIALIZER"
            sh request.sh $HOST $EXECUTION_VOLUME $PARAMS

            sleep 2

            # EXECUTE METRICS
            source cpuResourceUtilization.sh
            source memoryResourceUtilization.sh
            source responseTime.sh
            source transactionsPerSecond.sh

            # GENERATE CSV
            METRICS_FILE="metrics.csv"

            # CHECK FILE EXISTS
            if [ ! -f "$METRICS_FILE" ]; then
                echo "Broker,Serializer,Execution ID,Execution Volume,CPU Usage,Memory Usage,Response Time,Transactions Per Second" > "$METRICS_FILE"
            fi

            # ADD VALUES TO CSV
            echo "$BROKER,$SERIALIZER,$EXECUTION_NUMBER,$EXECUTION_VOLUME,$CPU_USAGE,$MEMORY_USAGE,$RESPONSE_TIME,$TPS" >> "$METRICS_FILE"

            echo "Arquivo CSV '$METRICS_FILE' atualizado com sucesso."
        done
    done
done