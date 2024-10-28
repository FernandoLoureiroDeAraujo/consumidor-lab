import os
import requests
from dotenv import load_dotenv

# Carrega as variáveis de ambiente do arquivo vars.sh
load_dotenv(dotenv_path='vars.sh')

# Obtém as variáveis de ambiente
HOST = os.getenv('HOST')
TRACE_ID = os.getenv('TRACE_ID')

# Verifica se as variáveis foram corretamente carregadas
if not HOST or not TRACE_ID:
    print("Erro ao carregar as variáveis de ambiente")
    exit(1)

# Configura a URL do Jaeger para obter o trace
url = f"{HOST}:16686/api/traces/{TRACE_ID}"

# Faz a requisição para obter o JSON do trace
response = requests.get(url)
if response.status_code != 200:
    print("Erro ao obter os dados do trace")
    exit(1)

# Converte a resposta para JSON
trace_json = response.json()

# Extrai o startTime do primeiro span e o startTime + duration do último span
spans = sorted(trace_json['data'][0]['spans'], key=lambda x: x['startTime'])
first_start_time = spans[0]['startTime']
last_span = spans[-1]
last_start_time = last_span['startTime']
last_duration = last_span['duration']

# Calcula o tempo final do último span
last_end_time = last_start_time + last_duration

# Calcula o número total de spans
total_spans = len(spans)

# Calcula a duração total do trace em microssegundos
trace_duration = last_end_time - first_start_time

# Converte a duração total para segundos (1 microssegundo = 1e6 nanossegundos)
trace_duration_in_seconds = trace_duration / 1_000_000

# Calcula a média de spans por segundo
spans_per_second = total_spans / trace_duration_in_seconds

# Exibe os resultados
print(f"Transactions Per Second: {spans_per_second:.6f}")