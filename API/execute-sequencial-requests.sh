#!/bin/bash

# URL base
base_url="http://localhost:8080/scheduler/send-data"

# Lista de variações para message-broker-type e message-format-type
message_brokers=("JMS" "KAFKA")
message_formats=("JSON" "PROTOBUF" "PROTOSTUFF" "KRYO" "MSGPACK")
loop_size=25

# Itera sobre as variações dos parâmetros
for broker in "${message_brokers[@]}"; do
    for format in "${message_formats[@]}"; do
        # Monta a URL com os parâmetros
        url="${base_url}?loop-size=${loop_size}&message-broker-type=${broker}&message-format-type=${format}"
        echo "URL: $url"
        curl -X GET "$url"
        echo
    done
done