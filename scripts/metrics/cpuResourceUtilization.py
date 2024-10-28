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
QUERY = "avg_over_time(process_cpu_usage[1d])"
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

# Converte para milicores (1 núcleo = 1000 milicores)
response_millicores = response_value * 1000

# Exibe o uso de CPU em milicores
print(f"CPU Usage: {response_millicores:.3f}")