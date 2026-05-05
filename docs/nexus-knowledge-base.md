# Nexus - Base de Conhecimento

## O que é o Nexus?

Nexus é uma plataforma de inteligência artificial que permite gerar conteúdo utilizando modelos de linguagem (LLMs) com suporte a RAG (Retrieval Augmented Generation). A aplicação foi desenvolvida em Java com Spring Boot e integra múltiplos providers de IA.

## Arquitetura

A aplicação segue uma arquitetura em camadas:

- **API Layer**: Controllers REST que recebem requisições HTTP
- **Service Layer**: Lógica de negócio e orquestração dos providers de IA
- **Infrastructure Layer**: Integração com vector stores e modelos de IA

O sistema utiliza o padrão RAG para enriquecer as respostas dos modelos com contexto relevante extraído de documentos previamente indexados.

## Providers de IA Disponíveis

### Ollama (Profile Local)

Ollama é uma ferramenta que permite rodar modelos de linguagem localmente. No Nexus, utilizamos:

- **Chat**: llama3 — modelo de linguagem da Meta com 8 bilhões de parâmetros, otimizado para conversação e geração de texto.
- **Embedding**: nomic-embed-text — modelo de embedding com 768 dimensões, usado para transformar texto em vetores numéricos.

Vantagens: privacidade total dos dados, sem custo por token, funciona offline.
Desvantagens: requer hardware com boa capacidade de processamento, respostas mais lentas que modelos cloud.

### Gemini (Profiles Dev/Prod)

Gemini é o modelo de IA do Google. No Nexus, utilizamos:

- **Chat**: gemini-2.5-flash — modelo rápido e eficiente para geração de texto, com boa relação custo-benefício.
- **Embedding**: text-embedding-004 — modelo de embedding do Google com 768 dimensões.

Vantagens: respostas rápidas, alta qualidade, bom para produção.
Desvantagens: requer API key, custo por token, dados enviados ao Google.

### Claude (Profiles Dev/Prod)

Claude é o modelo de IA da Anthropic. No Nexus, utilizamos:

- **Chat**: claude-sonnet-4-20250514 — modelo equilibrado entre velocidade e qualidade, excelente para análise e raciocínio.

Vantagens: excelente em tarefas de raciocínio, análise de código e escrita técnica.
Desvantagens: requer API key, custo por token, dados enviados à Anthropic.

## Vector Stores

### ChromaDB (Profile Local)

ChromaDB é um banco de dados vetorial open-source, leve e fácil de configurar. Ideal para desenvolvimento local e prototipagem.

- Porta padrão: 8000
- Collection: nexus-docs
- Inicialização automática do schema

### PGVector (Profiles Dev/Prod)

PGVector é uma extensão do PostgreSQL que adiciona suporte a vetores. Combina a robustez do PostgreSQL com busca vetorial.

- Tipo de índice: HNSW (Hierarchical Navigable Small World)
- Distância: cosine_distance
- Dimensões: 768
- Banco: nexus

## Como Funciona o RAG

O RAG (Retrieval Augmented Generation) funciona em duas etapas:

### 1. Ingestão de Documentos

1. O usuário faz upload de um documento (PDF ou Markdown) via `POST /api/nexus/documents`
2. O Apache Tika extrai o texto do documento
3. O TokenTextSplitter divide o texto em chunks menores
4. O modelo de embedding transforma cada chunk em um vetor numérico
5. Os vetores são armazenados no vector store (ChromaDB ou PGVector)

### 2. Consulta com Contexto

1. O usuário envia uma pergunta via `POST /api/nexus/generate`
2. O QuestionAnswerAdvisor busca os 5 chunks mais similares à pergunta no vector store
3. Os chunks encontrados são injetados como contexto no prompt
4. O LLM gera uma resposta baseada no contexto recuperado
5. A resposta é retornada com metadados de uso de tokens

## API Endpoints

### POST /api/nexus/generate

Gera conteúdo usando RAG com o provider escolhido.

Headers:
- `X-AI-Provider`: gemini, claude ou ollama (padrão: gemini)

Body:
```json
{
  "message": "Sua pergunta aqui"
}
```

Resposta:
```json
{
  "content": "Resposta gerada pelo modelo",
  "metadata": {
    "provider": "GEMINI",
    "model": "gemini-2.5-flash",
    "totalTokens": 150,
    "promptTokens": 100,
    "generationTokens": 50
  }
}
```

### POST /api/nexus/documents

Faz upload de documentos para ingestão no RAG.

Content-Type: multipart/form-data

Parâmetros:
- `file`: arquivo PDF ou Markdown

Resposta:
```json
{
  "filename": "documento.pdf",
  "chunks": 12,
  "message": "Documento processado e indexado com sucesso"
}
```

## Configuração de Profiles

| Profile | Comando |
|---------|---------|
| local   | `./mvnw spring-boot:run -Dspring-boot.run.profiles=local` |
| dev     | `GEMINI_API_KEY=... ANTHROPIC_API_KEY=... ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev` |
| prod    | Configurado via variáveis de ambiente no servidor |

## Requisitos do Profile Local

- Docker instalado e rodando
- Mínimo 8GB de RAM disponível para o Ollama
- Primeira execução demora mais pelo download dos modelos (~4GB)