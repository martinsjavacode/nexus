package io.github.martinsjavacode.nexus.service.impl;

import io.github.martinsjavacode.nexus.domain.NexusRequest;
import io.github.martinsjavacode.nexus.domain.NexusResponse;
import io.github.martinsjavacode.nexus.service.AiService;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GeminiAiService implements AiService {
	private final ChatModel chatModel;

	public GeminiAiService(ChatModel chatModel) {
		this.chatModel = chatModel;
	}

	@Override
	public NexusResponse generateContent(NexusRequest request) {
		// Building a prompt (simple for now)
		// We use a basic template for ensure future flexibility
		var template = new PromptTemplate("""
			Responda à seguinte pergunta de forma clara e direta:
			{message}
			""");

		Prompt prompt = template.create(
			Map.of("message", request.message())
		);

		// Call model (Gemini)
		final var chatResponse = chatModel.call(prompt);

		// Extraction Response and Metadata
		var content = chatResponse.getResult()
			.getOutput()
			.getText();

		// Extraction usage metadata (token) for observability
		// The Spring AI normalize this in Usage object
		var usage = chatResponse.getMetadata()
			.getUsage();

		Map<String, Object> metadata = Map.of(
			"model", "gemini-flash",
			"totalTokens", usage != null ? usage.getTotalTokens() : 0,
			"promptTokens", usage != null ? usage.getPromptTokens() : 0,
			"generationTokens", usage != null ? usage.getCompletionTokens() : 0
		);


		return new NexusResponse(content, metadata);
	}
}
