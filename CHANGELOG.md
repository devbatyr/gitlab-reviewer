# Changelog

## [1.0.0] - 2025-04-19

### Added
- 🎉 Initial release of **GitLab Reviewer**
- ✅ PMD integration to analyze Java code in GitLab Merge Requests
- 🔁 Webhook endpoint to trigger analysis automatically
- 📡 GitLab REST client for fetching MR details and posting comments
- 📄 Support for suppression rules and custom rulesets
- ⚙️ Environment-based configuration via `.env`
- 🧪 Unit tests for commit SHA extraction, diff parsing, and comment publishing

## [1.1.0] - 2025-04-19

### Added
- 🔥 AI integration using Ollama for generating recommendations based on PMD violations.
- 📦 Support for custom prompt templates (`prompt-template.txt`) with dynamic placeholder replacement.
- 🌡️ Configuration of Ollama model, temperature, and timeouts via `.env` and application config.
- 🧠 New `AiRecommendationService` for centralized interaction with the Ollama API.
