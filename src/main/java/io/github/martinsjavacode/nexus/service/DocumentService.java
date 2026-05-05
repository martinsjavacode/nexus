package io.github.martinsjavacode.nexus.service;

import org.springframework.web.multipart.MultipartFile;

public interface DocumentService {
	/**
	 * Processa um arquivo (PDF ou Markdown), extrai o texto,
	 * divide em chunks e armazena os embeddings no VectorStore.
	 *
	 * @param file o arquivo enviado via upload
	 * @return quantidade de chunks gerados e armazenados
	 */
	int ingestDocument(MultipartFile file);
}
