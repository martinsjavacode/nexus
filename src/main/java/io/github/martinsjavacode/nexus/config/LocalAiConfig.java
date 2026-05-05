package io.github.martinsjavacode.nexus.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Configuração de AI para o profile local.
 * Usa Ollama (llama3) como provider de chat e ChromaDB como vector store.
 */
@Configuration
@Profile("local")
public class LocalAiConfig {

	@Bean
	ChatClient ollamaChatClient(OllamaChatModel chatModel, VectorStore vectorStore) {
		return AiConfig.buildRagChatClient(chatModel, vectorStore);
	}
}