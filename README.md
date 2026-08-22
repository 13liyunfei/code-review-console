# code-review-console（管理控制台）

`code-review-agent` 的配套管理控制台（Spring Boot 微服务，默认端口 **8081**）。

- 以 `RestTemplate` 反向代理审查引擎（`code-review-agent`）的 `/api/admin/*` 管理接口；
- 托管前端（Vue 3 + ElementPlus）静态资源（`src/main/resources/static/`）；
- 多租户隔离：转发时自动携带 `X-Team-Id` 头。

> 本仓库为独立开源仓库，需与 [code-review-agent](https://github.com/your-org-or-user/code-review-agent) 配合使用。

## 技术栈

- Java 17、Spring Boot 3.3.4
- 前端：Vue 3 + Vite + ElementPlus（源码位于 `frontend/`）

## 快速开始

```bash
# 1) 启动审查引擎（另一仓库 code-review-agent，默认 :8080）
#    参见 code-review-agent 的 README

# 2) 启动控制台（默认 :8081）
cd code-review-console
./mvnw spring-boot:run
# 浏览器打开 http://localhost:8081
```

### 指向审查引擎

控制台通过配置项 `engine.base-url` 访问引擎，可用环境变量覆盖：

```bash
ENGINE_BASE_URL=http://localhost:8080 ./mvnw spring-boot:run
# 或在 application.yml 中修改 engine.base-url
```

默认 `http://localhost:8080`；容器/远程部署时改为实际地址。

## 前端开发（可选）

控制台已内置一份构建好的前端静态资源，开箱即用。如需自行修改前端：

```bash
cd frontend
npm install
npm run dev        # Vite 开发服务器，/api 代理到 8081
npm run build      # 构建产物输出到 ../src/main/resources/static
```

## License

本项目以 [MIT 协议](LICENSE) 开源。© 2026 13liyunfei。
