import os
import requests
from dotenv import load_dotenv

# Carrega as variáveis de ambiente do arquivo vars.sh
load_dotenv(dotenv_path='vars.sh')

# Obtém as variáveis de ambiente
HOST = os.getenv('HOST')
START_TIME = os.getenv('START_TIME')
END_TIME = os.getenv('END_TIME')

# Configura a URL do Prometheus e os parâmetros de consulta
PROMETHEUS_URL = f"{HOST}:9090"
QUERY = "avg_over_time(jvm_memory_used_bytes[1d]) / 1024 / 1024"  # Média em MB
STEP = "1d"

# Realiza a consulta HTTP para o Prometheus
params = {
    'query': QUERY,
    'start': START_TIME,
    'end': END_TIME,
    'step': STEP
}

response = requests.get(f"{PROMETHEUS_URL}/api/v1/query_range", params=params)
if response.status_code != 200:
    print("Erro ao obter os dados do Prometheus")
    exit(1)

# Extrai a resposta em JSON
data = response.json()

# Extrai o valor mais recente da resposta
try:
    response_value = float(data['data']['result'][0]['values'][-1][1])
except (IndexError, KeyError, ValueError) as e:
    print(f"Erro ao processar a resposta do JSON: {e}")
    exit(1)

# Exibe o uso de memória em MB
print(f"Memory Usage: {response_value}")