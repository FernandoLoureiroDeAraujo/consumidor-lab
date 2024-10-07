# Open Finance Consumer ExpRunA Lab

# Diagrama de solução
![![Experimental Solution Diagram.png](API%2FExperimental%20Solution%20Diagram.png)](API/solution-diagram.png "SOLUTION-DIAGRAM")

# Formas de executar
## 1 - Executar com docker compose
docker-compose -f consumer-compose.yml up -d
docker-compose --env-file expruna.env -f expruna-compose.yml up -d

## 2- Executar na AWS
Acesse a pasta "/scripts/terraform" e execute os comandos
terraform init
terraform plan
terraform apply

# Adicionais:
## Caso deseje adicionar mais scripts, será necessario buildar o dockerfile "expruna-cexpl.dockerfile"
docker build -t expruna_custom:1.0.0 -f scripts/expruna-cexpl.dockerfile .

## Instalar compilador de Bond
Instalação: Primeiro, você precisa instalar o pacote Bond. Você pode fazer isso usando o gerenciador de pacotes do Haskell, cabal ou stack. Aqui está um exemplo de como você pode instalar usando o cabal:
cabal update
cabal install bond

Uso: Depois de instalado, você pode usar o gbc para compilar seus arquivos Bond. O gbc é usado principalmente para gerar código para programas C++, C#, Java usando Bond. Aqui está um exemplo de como você pode usar o gbc:
gbc java your_file.bond

Este comando irá gerar o código Java para o arquivo Bond fornecido.