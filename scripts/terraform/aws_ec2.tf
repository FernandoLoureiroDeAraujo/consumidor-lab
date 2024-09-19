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
  instance_type = "t3.xlarge"              # Tipo de instância
  subnet_id     = aws_subnet.public[0].id  # Subnet pública

  # Use vpc_security_group_ids em vez de security_groups
  vpc_security_group_ids = [aws_security_group.allow_all.id]

  key_name      = aws_key_pair.deployer.key_name

  tags = {
    Name = "tf-arq-ec2-lab"
  }

  # Adicionar um disco EBS
  root_block_device {
    volume_type = "gp3"
    volume_size = 32  # Tamanho do disco em GB
  }

  # Instalação do Docker, Docker Compose e Git via script de provisionamento
  user_data = <<-EOF
              #!/bin/bash
              # Atualiza o sistema
              yum update -y
              
              # Instala o Git
              yum install git -y

              # Instala o Maven
              yum install maven -y

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
              git clone https://github.com/eneiascs/dsm-experiments.git /home/ec2-user/dsm-experiments

              # Define permissões apropriadas para os diretórios clonados
              chown -R ec2-user:ec2-user /home/ec2-user/consumidor-lab
              chown -R ec2-user:ec2-user /home/ec2-user/dsm-experiments

              # Ajusta trecho imagem docker errada no dsm-experiments
              sed -i 's#command: /bin/bash -c "/usr/local/bin/docker-entrypoint.sh"#command: /bin/bash -c "/opt/dohko/job/run"#' /home/ec2-user/dsm-experiments/docker-compose.yml

              # Inicia as aplicações                
              docker-compose -f /home/ec2-user/consumidor-lab/docker-compose.yml up -d
              docker-compose -f /home/ec2-user/dsm-experiments/docker-compose.yml up -d

              EOF
}

# Saída do IP público da instância
output "instance_public_ip" {
  description = "O IP público da instância EC2"
  value       = aws_instance.ec2_instance.public_ip
}