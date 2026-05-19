# Documentação do Sistema de Gestão de Vendas

## 1. Introdução

Este projeto é um **Sistema de Gestão de Vendas** desenvolvido em Java como trabalho acadêmico da disciplina de Programação II. O sistema gerencia dados de produtos, clientes, fornecedores, compras e vendas através de uma aplicação desktop com persistência em banco de dados PostgreSQL.

**Características principais:**
- Arquitetura em 3 camadas (Model-Controller-DAO)
- Persistência em banco de dados relacional
- CRUD (Create, Read, Update, Delete) para múltiplas entidades
- Validação de dados nas camadas de controle

---

## 2. Arquitetura do Projeto

O projeto segue o padrão **MVC (Model-View-Controller)** com separação clara de responsabilidades:

### 2.1 Estrutura de Camadas

```
com.luan.vendas
├── Main.java                    # Ponto de entrada
├── model/                       # Entidades do domínio
├── controller/                  # Lógica de negócio
└── dao/                         # Acesso aos dados
```

### 2.2 Camada de Modelo (Model)

Define as entidades do sistema:
- **Categoria**: Classificação de produtos (Peças, Rações, Veterinária, etc.)
- **Cliente**: Dados dos compradores (ID, Nome, CPF, Telefone, Endereço)
- **Fornecedor**: Dados dos fornecedores (ID, Nome, Empresa, CNPJ)
- **Produto**: Dados de produtos (ID, Nome, Preço Médio, Estoque, Categoria)
- **Venda**: Transações de venda com data, valor total e cliente
- **ProdutoVenda**: Itens dentro de uma venda (relação muitos-para-muitos)
- **Compra**: Transações de compra junto a fornecedores
- **CompraProduto**: Itens dentro de uma compra
- **FornecedorProduto**: Relação entre fornecedor e produtos

### 2.3 Camada de Controlador (Controller)

Implementa a lógica de negócio e validações:
- **CategoriaController**: Validações e operações CRUD de categorias
- **ClienteController**: Gerenciamento de clientes com validação de CPF
- **ProdutoController**: Controle de estoque e preços
- **VendaController**: Processamento de vendas
- **CompraController**: Processamento de compras
- Demais controllers especializados para operações específicas

**Exemplo de validação (CategoriaController):**
```java
public boolean salvarCategoria(int id, String nome) {
    if (id <= 0) return false;
    if (nome == null || nome.trim().isEmpty()) return false;
    // ... persistir categoria
}
```

### 2.4 Camada de Acesso a Dados (DAO)

Implementa o padrão **Data Access Object**:
- **CategoriaDao, ClienteDao, ProdutoDao, etc.**: Cada classe acessa uma tabela
- Operações básicas: `salvar()`, `alterar()`, `excluir()`, `listarTodos()`, `buscarPorId()`
- **Postgres.java**: Gerencia conexões com o banco de dados PostgreSQL

**Exemplo (CategoriaDao):**
```java
public boolean salvar(Categoria categoria) {
    String sql = "INSERT INTO tcategoria (id_categoria, nome_categoria) VALUES (?, ?)";
    try (Connection conn = Postgres.conectar();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, categoria.getId());
        ps.setString(2, categoria.getNome());
        return ps.executeUpdate() > 0;
    }
}
```

---

## 3. Tecnologias Utilizadas

| Tecnologia | Versão | Propósito |
|-----------|--------|----------|
| Java | 25 | Linguagem principal |
| PostgreSQL | 42.7.10 | Banco de dados |
| Maven | 3.15.0 | Gerenciamento de dependências |
| JUnit | 3.8.1 | Testes unitários |

---

## 4. Fluxo de Dados

### 4.1 Exemplo: Criação de uma Categoria

```
Main.java
    ↓
CategoriaController.salvarCategoria(1, "Peças")
    ↓ (validação de entrada)
CategoriaDao.salvar(categoriaObj)
    ↓ (SQL: INSERT INTO tcategoria)
PostgreSQL (persistência)
```

### 4.2 Exemplo: Criação de uma Venda

```
VendaController.salvarVenda(cliente, produtosVenda)
    ↓ (validação de cliente e produtos)
VendaDao.salvar(vendaObj)
    ↓ (SQL: INSERT INTO tvenda)
ProdutoVendaDao.salvar(produtoVendaObj)
    ↓ (SQL: INSERT INTO tproduto_venda para cada item)
PostgreSQL (persistência de venda + itens)
```

---

## 5. Modelo de Dados

### 5.1 Entidades Principais

```sql
tcategoria (id_categoria, nome_categoria)
tcliente (id_cliente, nome_cliente, cpf, telefone, endereco)
tfornecedor (id_fornecedor, nome_fornecedor, empresa, cnpj)
tproduto (id_produto, nome_produto, preco_medio, qtde_estoque, categoria_id)
tvenda (id_venda, data_venda, valor_total, cliente_id)
tproduto_venda (id_pv, id_venda, id_produto, quantidade, valor)
tcompra (id_compra, data_compra, valor_total, fornecedor_id)
tcompra_produto (id_cp, id_compra, id_produto, quantidade, valor)
```

### 5.2 Relacionamentos

- **Categoria** ← **Produto** (1:N)
- **Cliente** ← **Venda** (1:N)
- **Venda** ← **ProdutoVenda** (1:N)
- **Produto** ← **ProdutoVenda** (1:N)
- **Fornecedor** ← **Compra** (1:N)
- **Fornecedor** ← **FornecedorProduto** (N:M)

---

## 6. Configuração do Banco de Dados

A conexão é configurada via arquivo de propriedades:
- **Localização padrão**: `~/Desktop/Arquivos/db_vendas_upgrade.properties`
- **Variável de ambiente**: `DB_CONFIG_FILE`

**Exemplo de configuração:**
```properties
db.url=jdbc:postgresql://localhost:5432/vendas
db.user=postgres
db.password=senha
```

---

## 7. Funcionalidades Implementadas

### 7.1 Categorias
- ✅ Criar nova categoria
- ✅ Listar todas as categorias
- ✅ Buscar categoria por ID
- ✅ Alterar categoria
- ✅ Excluir categoria

### 7.2 Clientes
- ✅ Criar cliente com validação de CPF
- ✅ Listar clientes ordenados por nome
- ✅ Buscar cliente por ID
- ✅ Alterar dados de cliente
- ✅ Excluir cliente

### 7.3 Produtos
- ✅ Cadastrar produtos com controle de estoque
- ✅ Rastrear preço médio, última compra e última venda
- ✅ Listar produtos por categoria
- ✅ Atualizar informações de preço e estoque

### 7.4 Vendas
- ✅ Registrar venda com múltiplos produtos
- ✅ Calcular valor total automaticamente
- ✅ Registrar itens da venda (ProdutoVenda)
- ✅ Histórico de vendas por cliente

### 7.5 Compras e Fornecedores
- ✅ Registrar compra junto a fornecedores
- ✅ Rastrear itens de compra (CompraProduto)
- ✅ Gerenciar produtos por fornecedor (FornecedorProduto)
- ✅ Histórico de compras por fornecedor

---

## 8. Exemplo de Uso (Main.java)

O programa cria instâncias de dados de exemplo:

```java
// Criar categorias
CategoriaController categoria1 = new CategoriaController();
categoria1.salvarCategoria(1, "Peças");

// Criar clientes
ClienteController cliente1 = new ClienteController();
cliente1.salvarCliente(1, "Luan Morais", "123.456.789-00", 
                       "5612389", "Rua Minas Gerais, 123", "(64) 98765-4321");

// Criar fornecedores
FornecedorController fornecedor1 = new FornecedorController();
fornecedor1.salvarFornecedor(1, "Comigo", "Cooperativa de Crédito LTDA", 
                             "12.345.678/0001-00");

// Criar produtos
ProdutoController produto1 = new ProdutoController();
produto1.salvarProduto(1, "Ração Premium", 150.0, 100, 1);
```

---

## 9. Padrões e Boas Práticas

### 9.1 Padrão DAO
Encapsula toda a lógica de acesso a dados, permitindo trocar a implementação sem afetar a aplicação.

### 9.2 Validação em Camada de Negócio
Os controllers validam os dados antes de passar para o DAO, impedindo dados inválidos no banco.

### 9.3 PreparedStatements
Utiliza `PreparedStatement` para prevenir SQL Injection e melhorar performance.

### 9.4 Try-with-resources
Garante que conexões e statements sejam fechados automaticamente.

```java
try (Connection conn = Postgres.conectar();
     PreparedStatement ps = conn.prepareStatement(sql)) {
    // ... operação
} catch (SQLException e) {
    System.out.println("Erro: " + e.getMessage());
}
```

### 9.5 Tratamento de Exceções
Trata exceções SQL e registra mensagens de erro para debugging.

---

## 10. Dependências do Projeto

```xml
<!-- PostgreSQL JDBC Driver -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.7.10</version>
</dependency>

<!-- JUnit para Testes -->
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>3.8.1</version>
    <scope>test</scope>
</dependency>
```

---

## 11. Conclusão

Este sistema demonstra a aplicação prática dos conceitos de programação orientada a objetos, incluindo:
- Separação de responsabilidades (MVC)
- Padrão de projeto (DAO)
- Persistência de dados
- Validação e tratamento de erros
- Boas práticas de segurança (PreparedStatements)

O projeto serve como base educacional para compreender a arquitetura de uma aplicação Java com banco de dados.

---

**Autor**: Luan Morais  
**Disciplina**: Programação II  
**Versão**: 1.0-SNAPSHOT  
**Data**: Maio de 2026
