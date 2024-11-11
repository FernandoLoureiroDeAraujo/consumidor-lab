# Se nao tiver uma chave SSH gere com o seguinte comando
# ssh-keygen -t ed25519 -b 4096 -C "TF Arq Keypair" -f tf-arq-ec2-keypair

# Definir uma chave SSH existente para acesso
resource "aws_key_pair" "deployer" {
  key_name   = "ec2-keypair"
  public_key = file("~/.ssh/tf-arq-ec2-keypair.pub")  # Substitua pelo caminho da sua chave pública
}

# Criar uma Instância EC2
resource "aws_instance" "ec2_instance" {
  ami           = "ami-079d43025fa6a0145"  # AMI do Amazon Linux 2, verifique a AMI mais recente na sua região
  instance_type = "m5.2xlarge"              # Tipo de instância t3.xlarge
  subnet_id     = aws_subnet.public[0].id  # Subnet pública

  # Use vpc_security_group_ids em vez de security_groups
  vpc_security_group_ids = [aws_security_group.allow_all.id]

  key_name      = aws_key_pair.deployer.key_name

  tags = {
    Name = "Instancia_Estudo"
  }

  # Adicionar um disco EBS
  root_block_device {
    volume_type = "gp3"
    volume_size = 128  # Tamanho do disco em GB
  }

  # Instalação do Docker, Docker Compose e Git via script de provisionamento
  user_data = <<-EOF
              #!/bin/bash

              # Cria um arquivo de swap de 32GB
              fallocate -l 32G /swapfile
              chmod 600 /swapfile
              mkswap /swapfile
              swapon /swapfile
              echo '/swapfile swap swap defaults 0 0' >> /etc/fstab
              # Ajusta a prioridade do swap
              sysctl vm.swappiness=10
              echo 'vm.swappiness=10' >> /etc/sysctl.conf

              # Atualiza o sistema
              yum update -y
              
              # Instala o JQ, Git e Maven
              yum install jq git maven -y

              # Instala o Docker
              amazon-linux-extras install docker -y
              service docker start
              usermod -a -G docker ec2-user
              
              # Instala o Docker Compose
              curl -L "https://github.com/docker/compose/releases/download/v2.29.4/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
              chmod +x /usr/local/bin/docker-compose
              ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose
              docker-compose --version
              
              # Clona os repositórios do GitHub
              git clone https://github.com/FernandoLoureiroDeAraujo/consumidor-lab.git /home/ec2-user/consumidor-lab

              # Define permissões apropriadas para os diretórios clonados
              chown -R ec2-user:ec2-user /home/ec2-user/consumidor-lab

              # Prometheus Storage Config
              mkdir /tmp/prometheus
              chown 65534:65534 /tmp/prometheus

              # Jaeger Storage Config
              mkdir /tmp/badger
              chown 65534:65534 /tmp/badger

              # Inicia as aplicações
              docker-compose -f /home/ec2-user/consumidor-lab/consumer-compose.yml up -d
              #docker-compose --env-file /home/ec2-user/consumidor-lab/expruna.env -f /home/ec2-user/consumidor-lab/expruna-compose.yml up -d

              EOF
}


# Regra adicional para permitir o IP da instância EC2
resource "aws_security_group_rule" "allow_ec2_ip" {
  type              = "ingress"
  from_port         = 0
  to_port           = 65535
  protocol          = "tcp"
  cidr_blocks = [
    "${aws_instance.ec2_instance.private_ip}/32",  # Libera o IP privado da instância EC2
    "${aws_instance.ec2_instance.public_ip}/32"    # Libera o IP publico da instância EC2
  ]
  security_group_id = aws_security_group.allow_all.id

  # Garante que esta regra seja criada após a instância EC2
  depends_on = [aws_instance.ec2_instance]
}

# Saída do IP público da instância
output "instance_public_ip" {
  description = "O IP público da instância EC2"
  value       = aws_instance.ec2_instance.public_ip
}