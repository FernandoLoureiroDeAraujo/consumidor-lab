# Consumidor Lab

# Solution Diagram
![![Experimental Solution Diagram.png](API%2FExperimental%20Solution%20Diagram.png)](API/solution-diagram.png "SOLUTION-DIAGRAM")

# Run all applications
docker compose up --build

# Instalar compilador de Bond
Instalação: Primeiro, você precisa instalar o pacote Bond. Você pode fazer isso usando o gerenciador de pacotes do Haskell, cabal ou stack. Aqui está um exemplo de como você pode instalar usando o cabal:
cabal update
cabal install bond

Uso: Depois de instalado, você pode usar o gbc para compilar seus arquivos Bond. O gbc é usado principalmente para gerar código para programas C++, C#, Java usando Bond. Aqui está um exemplo de como você pode usar o gbc:
gbc java your_file.bond

Este comando irá gerar o código Java para o arquivo Bond fornecido.