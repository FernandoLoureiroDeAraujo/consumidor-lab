# Provedor AWS
provider "aws" {
  region = "sa-east-1"  # Altere para a região desejada
}

# Criar uma VPC
resource "aws_vpc" "main" {
  cidr_block = "10.0.0.0/16"
  enable_dns_support = true
  enable_dns_hostnames = true
  tags = {
    Name = "tf-arq-vpc"
  }
}

# Criar um Internet Gateway
resource "aws_internet_gateway" "main" {
  vpc_id = aws_vpc.main.id
  tags = {
    Name = "tf-arq-igw"
  }
}

# Criar uma Tabela de Roteamento para a Subnet Pública
resource "aws_route_table" "public" {
  vpc_id = aws_vpc.main.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.main.id
  }

  tags = {
    Name = "tf-arq-public-route-table"
  }
}

# Associar a Tabela de Roteamento à Subnet Pública
resource "aws_route_table_association" "public" {
  count          = length(aws_subnet.public)
  subnet_id      = aws_subnet.public[count.index].id
  route_table_id = aws_route_table.public.id
}

resource "aws_subnet" "public" {
  count             = 2  # Duas subnets públicas
  vpc_id            = aws_vpc.main.id
  cidr_block        = "10.0.${count.index + 1}.0/24" # Subnet publica com 256 IPs
  availability_zone = element(data.aws_availability_zones.available.names, count.index)
  map_public_ip_on_launch = true
  tags = {
    Name                                        = "tf-arq-public-subnet-${count.index}"
  }
}

# Associar a Tabela de Roteamento à Subnet Privada
resource "aws_route_table_association" "private" {
  count          = length(aws_subnet.private)
  subnet_id      = aws_subnet.private[count.index].id
  route_table_id = aws_route_table.private.id
}

resource "aws_subnet" "private" {
  count             = 2  # Duas subnets privadas
  vpc_id            = aws_vpc.main.id
  cidr_block        = "10.0.${count.index + 3}.0/24" # Subnet privada com 256 IPs
  availability_zone = element(data.aws_availability_zones.available.names, count.index)
  tags = {
    Name                                        = "tf-arq-private-subnet-${count.index}"
  }
}

# Criar uma Tabela de Roteamento para a Subnet Privada
resource "aws_route_table" "private" {
  vpc_id = aws_vpc.main.id

  # Sem rotas para a Internet Gateway, pois é uma subnet privada
  tags = {
    Name = "tf-arq-private-route-table"
  }
}

data "aws_availability_zones" "available" {}

# Criar um Security Group
resource "aws_security_group" "allow_all" {
  vpc_id = aws_vpc.main.id

  # Libera todas as portas (para fins de teste; ajuste conforme necessário)
  ingress {
    from_port   = 0
    to_port     = 65535
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # Pode sair para qualquer lugar
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "tf-arq-sg-allow-all"
  }
}

# Saída do ID da VPC
output "vpc_id" {
  description = "O ID da VPC"
  value       = aws_vpc.main.id
}

# Saída do ID da Subnet Pública
output "public_subnet_id" {
  description = "O ID da Subnet Pública"
  value       = aws_subnet.public[*].id
}

# Saída do ID da Subnet Privada
output "private_subnet_id" {
  description = "O ID da Subnet Privada"
  value       = aws_subnet.private[*].id
}