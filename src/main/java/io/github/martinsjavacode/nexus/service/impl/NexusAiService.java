package io.github.martinsjavacode.nexus.service.impl;

import io.github.martinsjavacode.nexus.domain.NexusRequest;
import io.github.martinsjavacode.nexus.domain.NexusResponse;
import io.github.martinsjavacode.nexus.domain.enums.ProviderType;
import io.github.martinsjavacode.nexus.service.AiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class NexusAiService implements AiService {

	private static final Logger log = LoggerFactory.getLogger(NexusAiService.class);

	private final Optional<ChatClient> geminiChatClient;
	private final Optional<ChatClient> claudeChatClient;
	private final Optional<ChatClient> ollamaChatClient;

	/**
	 * Injeta os ChatClients disponíveis no profile ativo.
	 * No profile local: apenas ollamaChatClient está presente.
	 * Nos profiles dev/prod: geminiChatClient e claudeChatClient estão presentes.
	 * O @Autowired(required = false) + Optional evita erro quando o bean não existe.
	 */
	public NexusAiService(
		@Autowired(required = false) @Qualifier("geminiChatClient") ChatClient geminiChatClient,
		@Autowired(required = false) @Qualifier("claudeChatClient") ChatClient claudeChatClient,
		@Autowired(required = false) @Qualifier("ollamaChatClient") ChatClient ollamaChatClient
	) {
		this.geminiChatClient = Optional.ofNullable(geminiChatClient);
		this.claudeChatClient = Optional.ofNullable(claudeChatClient);
		this.ollamaChatClient = Optional.ofNullable(ollamaChatClient);
	}

	@Override
	public NexusResponse generateContent(NexusRequest request, ProviderType provider) {
		ChatClient chatClient = resolveChatClient(provider);

		log.debug("Enviando prompt para o modelo. provider={}, model={}", provider, provider.getModel());
		long startTime = System.currentTimeMillis();

		ChatResponse chatResponse = chatClient.prompt()
			.user(request.message())
			.call()
			.chatResponse();

		long duration = System.currentTimeMillis() - startTime;

		var content = chatResponse.getResult()
			.getOutput()
			.getText();

		var usage = chatResponse.getMetadata().getUsage();

		Map<String, Object> metadata = Map.of(
			"provider", provider.name(),
			"model", provider.getModel(),
			"totalTokens", usage != null ? usage.getTotalTokens() : 0,
			"promptTokens", usage != null ? usage.getPromptTokens() : 0,
			"generationTokens", usage != null ? usage.getCompletionTokens() : 0
		);

		log.info("Resposta gerada. provider={}, model={}, totalTokens={}, durationMs={}",
			provider, provider.getModel(), metadata.get("totalTokens"), duration);

		return new NexusResponse(content, metadata);
	}

	private ChatClient resolveChatClient(ProviderType provider) {
		log.debug("Resolvendo ChatClient para provider={}", provider);
		return switch (provider) {
			case GEMINI -> geminiChatClient.orElseThrow(() ->
				new IllegalStateException("Gemini não está disponível neste profile. Use 'ollama'."));
			case CLAUDE -> claudeChatClient.orElseThrow(() ->
				new IllegalStateException("Claude não está disponível neste profile. Use 'ollama'."));
			case OLLAMA -> ollamaChatClient.orElseThrow(() ->
				new IllegalStateException("Ollama não está disponível neste profile. Use 'gemini' ou 'claude'."));
		};
	}
}