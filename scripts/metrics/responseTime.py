import os
import json
import subprocess
import urllib.request
from dotenv import load_dotenv

# Executa o script shell para carregar variáveis de ambiente
load_dotenv(dotenv_path='vars.sh')

# Obter as variáveis do ambiente
HOST = os.getenv('HOST')
TRACE_ID = os.getenv('TRACE_ID')

# Monta a URL da requisição
url = "{}:16686/api/traces/{}".format(HOST, TRACE_ID)

try:
    # Faz a requisição para obter o JSON
    with urllib.request.urlopen(url) as response:
        if response.status != 200:
            print(f"Erro ao fazer a requisição: {response.status}")
            exit(1)

        # Lê e decodifica os dados
        json_data = json.loads(response.read().decode("utf-8"))
except Exception as e:
    print(f"Erro ao fazer a requisição: {e}")
    exit(1)

# Extrai os tempos de início do primeiro e do último span
spans = sorted(json_data['data'][0]['spans'], key=lambda x: x['startTime'])
first_start_time = spans[0]['startTime']
last_start_time = spans[-1]['startTime']

# Verifica se os tempos foram corretamente extraídos
if first_start_time is None or last_start_time is None:
    print("Erro ao extrair os tempos do JSON")
    exit(1)

# Calcula a diferença entre o primeiro e o último startTime (em microssegundos)
duration = last_start_time - first_start_time

# Converte para segundos (1 microssegundo = 1e6 nanossegundos)
duration_seconds = duration / 1000000

# Exibe o tempo total do trace em segundos
print("Response Time: {:.6f}s".format(duration_seconds))