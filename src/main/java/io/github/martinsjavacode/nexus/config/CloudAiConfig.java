package io.github.martinsjavacode.nexus.config;

import org.springframework.ai.anthropic.AnthropicChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Configuração de AI para profiles dev e prod.
 * Usa Gemini e Claude como providers de chat e PGVector como vector store.
 */
@Configuration
@Profile("dev | prod")
public class CloudAiConfig {

	@Bean
	ChatClient geminiChatClient(GoogleGenAiChatModel chatModel, VectorStore vectorStore) {
		return AiConfig.buildRagChatClient(chatModel, vectorStore);
	}

	@Bean
	ChatClient claudeChatClient(AnthropicChatModel chatModel, VectorStore vectorStore) {
		return AiConfig.buildRagChatClient(chatModel, vectorStore);
	}
}