FROM eneiascs/hylaa:1.0.0

RUN apt update
RUN apt install jq curl bc nano git zlib1g-dev -y

# Update SSL to 1.1.1
RUN wget https://www.openssl.org/source/openssl-1.1.1g.tar.gz && \
    tar zxvf openssl-1.1.1g.tar.gz && \
    cd openssl-1.1.1g && \
    ./config --prefix=/home/root/openssl --openssldir=/home/root/openssl no-ssl2 && \
    make && \
    make install

RUN echo 'export PATH=/home/root/openssl/bin:$PATH' >> ~/.bashrc && \
    echo 'export LD_LIBRARY_PATH=/home/root/openssl/lib' >> ~/.bashrc && \
    echo 'export LDFLAGS="-L /home/root/openssl/lib -Wl,-rpath,/home/root/openssl/lib"' >> ~/.bashrc

# Install Pyenv - Ubuntu Version too old for apt get python
RUN curl https://pyenv.run | bash
RUN echo 'export PYENV_ROOT="/home/root/.pyenv"' >> ~/.bashrc && \
    echo 'command -v pyenv >/dev/null || export PATH="$PYENV_ROOT/bin:$PATH"' >> ~/.bashrc && \
    echo 'eval "$(pyenv init -)"' >> ~/.bashrc

# Adiciona o pyenv ao PATH
ENV PATH="/root/.pyenv/bin:${PATH}"
RUN eval "$(pyenv init --path)" && eval "$(pyenv init -)"

# Install Python 3.12.7
RUN LDFLAGS="-Wl,-rpath,/home/root/openssl/lib" CONFIGURE_OPTS="--with-openssl=/home/root/openssl --with-openssl-rpath=auto" pyenv install -v 3.12.7
RUN /root/.pyenv/versions/3.12.7/bin/pip3 install requests python-dotenv

COPY metrics/* /opt/dohko/job/

RUN chmod +x /opt/dohko/job/*