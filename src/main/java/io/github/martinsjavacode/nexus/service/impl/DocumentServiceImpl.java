package io.github.martinsjavacode.nexus.service.impl;

import io.github.martinsjavacode.nexus.service.DocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class DocumentServiceImpl implements DocumentService {

	private static final Logger log = LoggerFactory.getLogger(DocumentServiceImpl.class);

	private final VectorStore vectorStore;

	public DocumentServiceImpl(VectorStore vectorStore) {
		this.vectorStore = vectorStore;
	}

	@Override
	public int ingestDocument(MultipartFile file) {
		Resource resource = file.getResource();
		var reader = new TikaDocumentReader(resource);

		log.debug("Lendo documento com Tika. filename={}", file.getOriginalFilename());
		List<Document> documents = reader.get();
		log.debug("Documento lido. pages={}", documents.size());

		var splitter = new TokenTextSplitter();
		List<Document> chunks = splitter.apply(documents);
		log.debug("Documento dividido em chunks. totalChunks={}", chunks.size());

		log.info("Armazenando chunks no VectorStore. filename={}, chunks={}",
			file.getOriginalFilename(), chunks.size());
		vectorStore.add(chunks);

		return chunks.size();
	}
}