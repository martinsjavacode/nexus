#!/bin/bash
# Instala/atualiza o Ollama localmente e baixa os modelos necessários

MIN_VERSION="0.6.0"

version_lt() {
  [ "$(printf '%s\n' "$1" "$2" | sort -V | head -n1)" = "$1" ] && [ "$1" != "$2" ]
}

install_ollama() {
  echo "Instalando/atualizando Ollama..."
  curl -fsSL https://ollama.com/install.sh | sh
}

if ! command -v ollama &> /dev/null; then
  echo "Ollama não encontrado. Instalando..."
  install_ollama
else
  CURRENT_VERSION=$(ollama --version 2>/dev/null | grep -oP '\d+\.\d+\.\d+' | head -1)
  if [ -z "$CURRENT_VERSION" ]; then
    echo "Não foi possível detectar a versão do Ollama. Atualizando..."
    install_ollama
  elif version_lt "$CURRENT_VERSION" "$MIN_VERSION"; then
    echo "Ollama $CURRENT_VERSION está desatualizado (mínimo: $MIN_VERSION). Atualizando..."
    install_ollama
  else
    echo "Ollama $CURRENT_VERSION já está instalado e atualizado."
  fi
fi

# Garante que o Ollama está rodando
if ! ollama list > /dev/null 2>&1; then
  echo "Iniciando Ollama..."
  ollama serve &
  sleep 3
fi

echo "Aguardando Ollama ficar pronto..."
until ollama list > /dev/null 2>&1; do
  sleep 1
done
echo "Ollama pronto."

echo "Baixando modelos..."
ollama pull llama3.2:3b
ollama pull nomic-embed-text
echo "Modelos prontos."