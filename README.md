# code-review-console (Management Console)

> [English](README.md) | [中文](README.zh-CN.md)

The companion management console for the [code-review-agent](https://github.com/13liyunfei/code-review-agent) engine (Spring Boot microservice, default port **8081**).

- Proxies the engine's `/api/admin/*` management endpoints via `RestTemplate`;
- Hosts the Vue 3 + ElementPlus frontend static assets (`src/main/resources/static/`);
- **Multi-tenancy**: automatically forwards the `X-Team-Id` header when calling the engine.

> Standalone open-source repository — pair it with the engine repository `code-review-agent`.

## Tech Stack

- Java 17, Spring Boot 3.3.4
- Frontend: Vue 3 + Vite + ElementPlus (source under `frontend/`)

## Quick Start

```bash
# 1) Start the review engine first (code-review-agent, default :8080)
#    See the code-review-agent README

# 2) Start the console (default :8081)
cd code-review-console
./mvnw spring-boot:run
# Open http://localhost:8081 in your browser
```

### Point the console at the engine

The console accesses the engine via the `engine.base-url` property (overridable by env var):

```bash
ENGINE_BASE_URL=http://localhost:8080 ./mvnw spring-boot:run
# Or edit engine.base-url in application.yml
```

Defaults to `http://localhost:8080`; use the actual address for container/remote deployments.

## Frontend Development (Optional)

The console ships with a pre-built frontend, ready to use. To develop the frontend yourself:

```bash
cd frontend
npm install
npm run dev        # Vite dev server, /api proxied to 8081
npm run build      # Output goes to ../src/main/resources/static
```

### Internationalization (i18n)

The console UI supports **中文 / English** switching. Click the language button in the top-right corner of the header; the choice is persisted in `localStorage` (`console-lang`).

- Language packs: `frontend/src/i18n/locales/zh.js` and `en.js`
- i18n wiring: `frontend/src/i18n/index.js` (vue-i18n 9, legacy mode disabled)

## License

Open-sourced under the [MIT license](LICENSE). © 2026 code-review-agent contributors.
