#!/bin/bash

# Definindo a pasta raiz dos projetos
# Sobe um nivel para ConsumidorLab
RAIZ=$(dirname "$PWD")

# Percorrendo as subpastas que possuem o arquivo pom.xml
for projeto in $(find "$RAIZ" -type d -not -path '*/target/*' -exec test -e "{}/pom.xml" \; -print); do
    echo "Gerando versão para o projeto em: $projeto"

    # Executando o comando Maven
    (cd "$projeto" && mvn clean package -Dmaven.test.skip=true -Dmaven.javadoc.skip=true && mkdir target/dependency && (cd target/dependency; jar -xf ../*.jar))
done

# Subindo o Docker Compose
echo "Subindo Docker Compose..."
# docker-compose up --no-deps --build --force-recreate