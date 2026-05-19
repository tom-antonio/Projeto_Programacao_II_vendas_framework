# Guia de Setup e Execução

## Pré-requisitos

- Java 25 ou superior
- Maven 3.6+
- PostgreSQL 12+
- Git

## 1. Clonar o Repositório

```bash
git clone https://github.com/luanmorais/Projeto_Programacao_II_vendas_upgrate.git
cd Projeto_Programacao_II_vendas_upgrate/vendas
```

## 2. Configurar Banco de Dados

### 2.1 Criar arquivo de configuração

Crie o arquivo `~/Desktop/Arquivos/db_vendas_upgrade.properties`:

```properties
db.url=jdbc:postgresql://localhost:5432/vendas
db.user=postgres
db.password=sua_senha
db.driver=org.postgresql.Driver
```

### 2.2 Criar banco de dados

```sql
-- Conectar ao PostgreSQL
psql -U postgres

-- Criar banco de dados
CREATE DATABASE vendas;

-- Conectar ao banco
\c vendas

-- Criar tabelas
CREATE TABLE tcategoria (
    id_categoria SERIAL PRIMARY KEY,
    nome_categoria VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE tcliente (
    id_cliente SERIAL PRIMARY KEY,
    nome_cliente VARCHAR(150) NOT NULL,
    cpf VARCHAR(14) UNIQUE,
    telefone VARCHAR(15),
    endereco VARCHAR(200),
    email VARCHAR(100)
);

CREATE TABLE tfornecedor (
    id_fornecedor SERIAL PRIMARY KEY,
    nome_fornecedor VARCHAR(150) NOT NULL,
    empresa VARCHAR(150),
    cnpj VARCHAR(18) UNIQUE,
    contato VARCHAR(15),
    email VARCHAR(100)
);

CREATE TABLE tproduto (
    id_produto SERIAL PRIMARY KEY,
    nome_produto VARCHAR(150) NOT NULL,
    preco_medio DECIMAL(10,2),
    qtde_estoque DECIMAL(10,2),
    valor_ultima_compra DECIMAL(10,2),
    valor_ultima_venda DECIMAL(10,2),
    categoria_id INT REFERENCES tcategoria(id_categoria)
);

CREATE TABLE tvenda (
    id_venda SERIAL PRIMARY KEY,
    data_venda TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    valor_total DECIMAL(12,2),
    cliente_id INT REFERENCES tcliente(id_cliente)
);

CREATE TABLE tproduto_venda (
    id_pv SERIAL PRIMARY KEY,
    id_venda INT REFERENCES tvenda(id_venda),
    id_produto INT REFERENCES tproduto(id_produto),
    quantidade DECIMAL(10,2),
    valor DECIMAL(10,2)
);

CREATE TABLE tcompra (
    id_compra SERIAL PRIMARY KEY,
    data_compra TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    valor_total DECIMAL(12,2),
    fornecedor_id INT REFERENCES tfornecedor(id_fornecedor)
);

CREATE TABLE tcompra_produto (
    id_cp SERIAL PRIMARY KEY,
    id_compra INT REFERENCES tcompra(id_compra),
    id_produto INT REFERENCES tproduto(id_produto),
    quantidade DECIMAL(10,2),
    valor DECIMAL(10,2)
);

CREATE TABLE tfornecedor_produto (
    id_fp SERIAL PRIMARY KEY,
    fornecedor_id INT REFERENCES tfornecedor(id_fornecedor),
    produto_id INT REFERENCES tproduto(id_produto)
);
```

## 3. Compilar o Projeto

```bash
mvn clean compile
```

## 4. Executar Testes

```bash
mvn test
```

## 5. Executar a Aplicação

```bash
mvn exec:java -Dexec.mainClass="com.luan.vendas.Main"
```

Ou executar direto pela IDE (IntelliJ IDEA, Eclipse):
1. Abra o projeto
2. Localize `Main.java`
3. Clique com botão direito e selecione "Run"

## Estrutura de Diretórios

```
vendas/
├── pom.xml                          # Configuração Maven
├── sql/                             # Scripts SQL (vazio)
├── src/
│   ├── main/
│   │   └── java/com/luan/vendas/
│   │       ├── Main.java            # Ponto de entrada
│   │       ├── model/               # Entidades
│   │       ├── controller/          # Lógica de negócio
│   │       └── dao/                 # Acesso a dados
│   └── test/
│       └── java/                    # Testes unitários
├── target/                          # Arquivos compilados
└── DOCUMENTACAO.md                  # Esta documentação
```

## Variáveis de Ambiente (Opcional)

Para usar um caminho customizado para o arquivo de configuração:

```bash
export DB_CONFIG_FILE=/caminho/para/arquivo.properties
```

## Troubleshooting

### Erro: "Arquivo de configuração não encontrado"
- Certifique-se de que `~/Desktop/Arquivos/db_vendas_upgrade.properties` existe
- Ou defina a variável `DB_CONFIG_FILE`

### Erro: "Connection refused"
- Verifique se PostgreSQL está rodando
- Confirme as credenciais no arquivo de configuração
- Teste a conexão: `psql -U postgres -h localhost`

### Erro: "Classe não encontrada"
- Execute `mvn clean install` para garantir que dependências foram baixadas
- Verifique se tem Java 25+ instalado: `java -version`

## Build para Produção

```bash
mvn clean package
```

Isso gera um JAR em `target/vendas-1.0-SNAPSHOT.jar`

## Exemplos de Uso

Ver arquivo `DOCUMENTACAO.md` seção 8 para exemplos de código.

---

**Última atualização**: Maio de 2026
