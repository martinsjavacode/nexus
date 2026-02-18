package io.github.martinsjavacode.nexus.service;

import io.github.martinsjavacode.nexus.domain.NexusRequest;
import io.github.martinsjavacode.nexus.domain.NexusResponse;

public interface AiService {
	NexusResponse generateContent(NexusRequest request);
}
