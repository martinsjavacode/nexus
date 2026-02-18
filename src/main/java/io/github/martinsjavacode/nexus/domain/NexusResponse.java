package io.github.martinsjavacode.nexus.domain;

import java.util.Map;

public record NexusResponse(
	String content,
	Map<String, Object> metadata
) {
}
