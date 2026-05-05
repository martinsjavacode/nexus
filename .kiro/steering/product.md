# Product Overview

Nexus is an experimental, incremental Java project for exploring modern AI techniques in the Java/Spring ecosystem. It serves as a learning platform for backend engineers to apply LLMs, prompt engineering, embeddings, RAG, conversational memory, and AI agents in a production-style architecture.

The project evolves a single codebase through progressive challenges rather than isolated examples, emphasizing real engineering decisions, clean architecture, and extensibility.

## Current State

- Exposes a REST API (`POST /api/nexus/generate`) that accepts a user message and returns AI-generated content with token usage metadata.
- Integrates with Google Gemini (gemini-2.5-flash) via Spring AI.
- Early stage — single endpoint, single AI provider, no persistence or conversation history yet.

## Language

The README and prompt templates are written in Portuguese (Brazilian). Maintain this convention for user-facing text and documentation unless directed otherwise. Code identifiers and comments are in English.
