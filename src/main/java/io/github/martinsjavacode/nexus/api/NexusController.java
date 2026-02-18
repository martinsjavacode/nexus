package io.github.martinsjavacode.nexus.api;

import io.github.martinsjavacode.nexus.domain.NexusRequest;
import io.github.martinsjavacode.nexus.domain.NexusResponse;
import io.github.martinsjavacode.nexus.service.AiService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/nexus")
public class NexusController {
	private final AiService aiService;

	public NexusController(AiService geminiAiService) {
		this.aiService = geminiAiService;
	}

	@PostMapping("generate")
	public ResponseEntity<NexusResponse> generateResponse(@RequestBody @Valid NexusRequest request) {
		final var nexusResponse = aiService.generateContent(request);
		return ResponseEntity.ok(nexusResponse);
	}
}
