package io.github.martinsjavacode.nexus.domain.enums;

public enum ProviderType {
	GEMINI("gemini-2.5-flash"),
	CLAUDE("claude-sonnet-4-20250514"),
	OLLAMA("llama3");

	private final String model;

	ProviderType(String model) {
		this.model = model;
	}

	public String getModel() {
		return model;
	}

	/**
	 * Converte o valor do header X-AI-Provider para o enum.
	 * Aceita qualquer case (ex: "gemini", "GEMINI", "Gemini").
	 *
	 * @param value valor recebido no header
	 * @return o ProviderType correspondente
	 * @throws IllegalArgumentException se o valor não corresponder a nenhum provider
	 */
	public static ProviderType fromHeader(String value) {
		try {
			return valueOf(value.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(
				"Provider desconhecido: '%s'. Use 'gemini', 'claude' ou 'ollama'.".formatted(value)
			);
		}
	}
}