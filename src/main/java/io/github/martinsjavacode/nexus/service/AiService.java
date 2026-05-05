package io.github.martinsjavacode.nexus.service;

import io.github.martinsjavacode.nexus.domain.NexusRequest;
import io.github.martinsjavacode.nexus.domain.NexusResponse;
import io.github.martinsjavacode.nexus.domain.enums.ProviderType;

public interface AiService {
	/**
	 * Gera conteúdo usando RAG: busca contexto relevante no VectorStore
	 * e envia ao LLM escolhido pelo provider.
	 *
	 * @param request  a pergunta do usuário
	 * @param provider o provider de AI
	 * @return resposta gerada com metadados
	 */
	NexusResponse generateContent(NexusRequest request, ProviderType provider);
}