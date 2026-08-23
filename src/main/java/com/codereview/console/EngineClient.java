package com.codereview.console;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * 代码审查引擎客户端：以 RestTemplate 发起微服务调用（HTTP）。
 *
 * <p>所有请求统一转发到 {@code engine.base-url} 指向的引擎管理接口；
 * 出错时返回含错误信息的 JSON，避免前端拿到非 JSON 响应。
 */
@Component
public class EngineClient {

    private final RestTemplate rest = new RestTemplate();
    private final String baseUrl;

    public EngineClient(@Value("${engine.base-url:http://localhost:8080}") String baseUrl) {
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    }

    public String get(String path) {
        return get(path, null);
    }

    /**
     * GET 转发到引擎，可选地携带 {@code X-Team-Id} 头（多租户隔离）。
     */
    public String get(String path, String teamId) {
        try {
            HttpEntity<Void> request = new HttpEntity<>(teamHeaders(teamId));
            ResponseEntity<String> r = rest.exchange(baseUrl + path, HttpMethod.GET, request, String.class);
            return r.getBody();
        } catch (HttpClientErrorException e) {
            return passthroughError("GET " + path, e);
        } catch (Exception e) {
            return errorJson("GET " + path, e);
        }
    }

    public String post(String path, Object body) {
        return post(path, body, null);
    }

    /**
     * POST 转发到引擎，可选地携带 {@code X-Team-Id} 头（多租户隔离）。
     */
    public String post(String path, Object body, String teamId) {
        try {
            HttpHeaders headers = teamHeaders(teamId);
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Object> request = new HttpEntity<>(body, headers);
            ResponseEntity<String> r = rest.exchange(baseUrl + path, HttpMethod.POST, request, String.class);
            return r.getBody();
        } catch (HttpClientErrorException e) {
            return passthroughError("POST " + path, e);
        } catch (Exception e) {
            return errorJson("POST " + path, e);
        }
    }

    public String delete(String path) {
        return delete(path, null);
    }

    /**
     * DELETE 转发到引擎，可选地携带 {@code X-Team-Id} 头（多租户隔离）。
     */
    public String delete(String path, String teamId) {
        try {
            rest.exchange(baseUrl + path, HttpMethod.DELETE, new HttpEntity<>(teamHeaders(teamId)), Void.class);
            return "{\"deleted\":true}";
        } catch (HttpClientErrorException e) {
            return passthroughError("DELETE " + path, e);
        } catch (Exception e) {
            return errorJson("DELETE " + path, e);
        }
    }

    /**
     * PUT 转发到引擎（用于自定义 Agent 的编辑更新），携带 {@code X-Team-Id} 头（多租户隔离）。
     *
     * <p>引擎侧命中注入预检 / 乐观锁冲突会返回 4xx + 明确 JSON 错误体，
     * 这里透传引擎原始响应体，让前端能直接展示「被拒绝的原因」。
     */
    public String put(String path, Object body, String teamId) {
        try {
            HttpHeaders headers = teamHeaders(teamId);
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Object> request = new HttpEntity<>(body, headers);
            ResponseEntity<String> r = rest.exchange(baseUrl + path, HttpMethod.PUT, request, String.class);
            return r.getBody();
        } catch (HttpClientErrorException e) {
            return passthroughError("PUT " + path, e);
        } catch (Exception e) {
            return errorJson("PUT " + path, e);
        }
    }

    /**
     * 发送原始文本体（Content-Type: text/plain），用于 YAML 等需整段文本的规则导入接口。
     */
    public String postRaw(String path, String body) {
        return postRaw(path, body, null);
    }

    /**
     * POST 原始文本（text/plain）转发到引擎，可选地携带 {@code X-Team-Id} 头（多租户隔离）。
     */
    public String postRaw(String path, String body, String teamId) {
        try {
            HttpHeaders headers = teamHeaders(teamId);
            headers.setContentType(MediaType.TEXT_PLAIN);
            HttpEntity<String> request = new HttpEntity<>(body, headers);
            ResponseEntity<String> r = rest.exchange(baseUrl + path, HttpMethod.POST, request, String.class);
            return r.getBody();
        } catch (Exception e) {
            return errorJson("POST(raw) " + path, e);
        }
    }

    /**
     * 透传 multipart 上传（文件 + 附加表单字段）到引擎。
     */
    public String upload(String path, MultipartFile file, Map<String, String> form) {
        return upload(path, file, form, null);
    }

    /**
     * 透传 multipart 上传（文件 + 附加表单字段）到引擎，可选地携带 {@code X-Team-Id} 头（多租户隔离）。
     */
    public String upload(String path, MultipartFile file, Map<String, String> form, String teamId) {
        try {
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            if (file != null && !file.isEmpty()) {
                final String name = file.getOriginalFilename();
                ByteArrayResource resource = new ByteArrayResource(file.getBytes()) {
                    @Override
                    public String getFilename() {
                        return name;
                    }
                };
                body.add("file", resource);
            }
            if (form != null) {
                form.forEach(body::add);
            }
            HttpHeaders headers = teamHeaders(teamId);
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
            ResponseEntity<String> r = rest.postForEntity(baseUrl + path, request, String.class);
            return r.getBody();
        } catch (IOException e) {
            return errorJson("UPLOAD " + path, e);
        } catch (Exception e) {
            return errorJson("UPLOAD " + path, e);
        }
    }

    /**
     * 构造请求头：当 {@code teamId} 非空时附加 {@code X-Team-Id}，供引擎按团队隔离。
     */
    private HttpHeaders teamHeaders(String teamId) {
        HttpHeaders headers = new HttpHeaders();
        if (teamId != null && !teamId.isBlank()) {
            headers.set("X-Team-Id", teamId);
        }
        return headers;
    }

    private String errorJson(String op, Throwable e) {
        String msg = e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage();
        msg = msg.replace("\"", "'");
        return "{\"error\":true,\"operation\":\"" + op + "\",\"message\":\"" + msg + "\"}";
    }

    /**
     * 引擎返回 4xx 时透传其原始 JSON 错误体（如注入预检 400 / 乐观锁 409），
     * 保留引擎给出的明确原因；无法解析时回退到 {@link #errorJson}。
     */
    private String passthroughError(String op, HttpClientErrorException e) {
        String body = e.getResponseBodyAsString();
        if (body != null && body.trim().startsWith("{")) {
            return body;
        }
        return errorJson(op, e);
    }
}
