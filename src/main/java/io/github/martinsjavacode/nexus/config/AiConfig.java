package io.github.martinsjavacode.nexus.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;

/**
 * Utilitário para criar ChatClients com o advisor de RAG configurado.
 * Usado pelas configurações específicas de cada profile.
 */
public final class AiConfig {

	private AiConfig() {
	}

	public static ChatClient buildRagChatClient(ChatModel chatModel, VectorStore vectorStore) {
		return ChatClient.builder(chatModel)
			.defaultAdvisors(
				QuestionAnswerAdvisor.builder(vectorStore)
					.searchRequest(
						SearchRequest.builder()
							.similarityThreshold(0.3)
							.topK(2)
							.build()
					).build()
			)
			.build();
	}
}