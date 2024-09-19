# eks.tf
resource "aws_eks_cluster" "eks_cluster" {
  name     = "tf-arq-eks-dynatrace"
  role_arn  = aws_iam_role.eks_cluster_role.arn
  version   = "1.30"  # Versão do EKS

  vpc_config {
    subnet_ids = concat(
      aws_subnet.public[*].id
    )
  }

  tags = {
    Name = "tf-arq-eks-cluster"
  }
}

resource "aws_eks_node_group" "eks_cluster" {
  cluster_name    = aws_eks_cluster.eks_cluster.name
  node_group_name = "tf-arq-eks-node-group"
  node_role_arn   = aws_iam_role.eks_node_role.arn
  subnet_ids      = aws_subnet.public[*].id
  instance_types  = ["t3.xlarge"]  # Tipo de instância dos nós

  scaling_config {
    desired_size = 1
    max_size     = 2
    min_size     = 1
  }

  # Definindo a política de atualização padrão (rolling update)
  update_config {
    max_unavailable = 1  # Número máximo de instâncias indisponíveis durante a atualização
  }

  depends_on = [
    aws_iam_role_policy_attachment.eks_node_policy,
    aws_iam_role_policy_attachment.eks_cni_policy,
    aws_iam_role_policy_attachment.ec2_container_registry_policy
  ]

  tags = {
    Name = "tf-arq-eks-node-group"
  }
}

resource "aws_iam_role" "eks_cluster_role" {
  name = "eks-cluster-role"
  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "eks.amazonaws.com"
        }
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "eks_cluster_policy" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKSClusterPolicy"
  role       = aws_iam_role.eks_cluster_role.name
}

resource "aws_iam_role" "eks_node_role" {
  name = "eks-node-role"
  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "ec2.amazonaws.com"
        }
      }
    ]
  })
}

# Anexar políticas ao node role
resource "aws_iam_role_policy_attachment" "eks_node_policy" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKSWorkerNodePolicy"
  role     = aws_iam_role.eks_node_role.name
}

resource "aws_iam_role_policy_attachment" "ec2_container_registry_policy" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonEC2ContainerRegistryReadOnly"
  role     = aws_iam_role.eks_node_role.name
}

# Política CNI para os nós do EKS
resource "aws_iam_role_policy_attachment" "eks_cni_policy" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKS_CNI_Policy"
  role       = aws_iam_role.eks_node_role.name
}

# Configurar Kubectl
# aws eks update-kubeconfig --region sa-east-1 --name tf-arq-eks-dynatrace