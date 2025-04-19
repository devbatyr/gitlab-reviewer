Gitlab Reviewer 🤖
======================

**Gitlab Reviewer** is an automated static analysis tool for GitLab. It uses PMD to analyze Java code in merge requests and automatically comments on violations directly in the MR.

This tool was designed as a lightweight, centralized solution to avoid integrating PMD and linters into dozens (or hundreds) of existing projects. Instead of modifying each repository's build or CI pipeline, Gitlab Reviewer offers a single point of analysis with consistent results.

The current version provides a minimal integration — it runs PMD, formats the output, and posts results. However, the architecture allows further customization: filtering specific rules, modifying the output, or injecting custom analysis logic.

🚀 Features
-----------
- Supports GitLab (REST API v4)
- Integrates with PMD 7.x
- Posts violations as comments in merge requests
- REST endpoint to receive GitLab webhooks
- Supports both:
    - Local development via `.env`
    - Docker (JVM and native-image)
- Configurable paths and tokens via environment variables

⚙️ Configuration
----------------
Copy `.env.example` to `.env` and update the values:

```bash
cp .env.example .env
```

```env
# Gitlab API
GITLAB_URL=https://gitlab.com
GITLAB_TOKEN=glpat-your-gitlab-token-here

# PMD Configuration
PMD_PATH=/absolute/path/to/pmd
PMD_RULESET=/absolute/path/to/ruleset.xml
PMD_SUPPRESSIONS=/absolute/path/to/suppressions.xml
```

🔧 Running Locally
------------------

```bash
./mvnw quarkus:dev
```

🐳 Docker (JVM)
---------------

```bash
docker build -t gitlab-reviewer-jvm -f Dockerfile.jvm .
docker run --env-file .env gitlab-reviewer-jvm
```

🐳 Docker (Native Image)
------------------------

```bash
docker build -t gitlab-reviewer-native -f Dockerfile.native .
docker run --env-file .env gitlab-reviewer-native
```

> ⚠️ Native image requires Linux with glibc ≥ 2.34 and `amd64` architecture.

📩 Webhook Endpoint
-------------------

Configure Gitlab to send a webhook to:

```
POST http://your-host/api/webhook
Content-Type: application/json
```

Payload example:

```json
{
  "projectId": 68609685,
  "mrIid": 2,
  "ref": "source-branch"
}
```

🛡️ Tech Stack
--------------

- Quarkus 3.5
- Java 21
- PMD 7.7.0

📄 License
----------

MIT © Batyrkhan Shakenov (https://github.com/devbatyr)