#!/bin/bash

source vars.sh

PROMETHEUS_URL="${HOST}:9090"
QUERY="sum(avg_over_time(process_cpu_usage[1d]))"
STEP=1d

RESPONSE=$(curl -s -G \
  --data-urlencode "query=$QUERY" \
  --data-urlencode "start=$START_TIME" \
  --data-urlencode "end=$END_TIME" \
  --data-urlencode "step=$STEP" \
  "$PROMETHEUS_URL/api/v1/query_range" | jq -r '.data.result[0].values[-1][1]')

export CPU_USAGE=$RESPONSE

printf "CPU Usage: %.6f\n" "$RESPONSE"