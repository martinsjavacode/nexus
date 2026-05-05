package io.github.martinsjavacode.nexus.api;

import io.github.martinsjavacode.nexus.domain.NexusRequest;
import io.github.martinsjavacode.nexus.domain.NexusResponse;
import io.github.martinsjavacode.nexus.domain.enums.ProviderType;
import io.github.martinsjavacode.nexus.service.AiService;
import io.github.martinsjavacode.nexus.service.DocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/nexus")
public class NexusController {

	private static final Logger log = LoggerFactory.getLogger(NexusController.class);

	private final AiService aiService;
	private final DocumentService documentService;

	public NexusController(AiService aiService, DocumentService documentService) {
		this.aiService = aiService;
		this.documentService = documentService;
	}

	/**
	 * Gera conteúdo usando RAG.
	 * O provider é escolhido via header X-AI-Provider (gemini ou ollama).
	 * Se não informado, usa gemini como padrão.
	 */
	@PostMapping("/generate")
	public ResponseEntity<NexusResponse> generateResponse(
		@RequestBody NexusRequest request,
		@RequestHeader(value = "X-AI-Provider", defaultValue = "gemini") String provider
	) {
		var providerType = ProviderType.fromHeader(provider);
		log.info("Requisição de geração recebida. provider={}, messageLength={}",
			providerType, request.message().length());

		var response = aiService.generateContent(request, providerType);

		log.info("Geração concluída. provider={}, totalTokens={}",
			providerType, response.metadata().get("totalTokens"));
		return ResponseEntity.ok(response);
	}

	/**
	 * Endpoint de upload de documentos para ingestão no RAG.
	 * Aceita PDFs e Markdowns.
	 * O documento é processado, dividido em chunks e armazenado no ChromaDB.
	 */
	@PostMapping(value = "/documents", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Map<String, Object>> uploadDocument(@RequestParam("file") MultipartFile file) {
		String filename = file.getOriginalFilename();

		if (filename == null || (!filename.endsWith(".pdf") && !filename.endsWith(".md"))) {
			log.warn("Upload rejeitado: formato não suportado. filename={}", filename);
			return ResponseEntity.badRequest().body(Map.of(
				"error", "Formato não suportado. Envie arquivos .pdf ou .md"
			));
		}

		log.info("Upload de documento iniciado. filename={}, size={}", filename, file.getSize());

		int chunks = documentService.ingestDocument(file);

		log.info("Documento indexado com sucesso. filename={}, chunks={}", filename, chunks);
		return ResponseEntity.ok(Map.of(
			"filename", filename,
			"chunks", chunks,
			"message", "Documento processado e indexado com sucesso"
		));
	}
}