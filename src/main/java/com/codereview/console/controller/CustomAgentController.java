package com.codereview.console.controller;

import com.codereview.console.EngineClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 自定义审查 Agent 代理：将前端请求透传到代码审查引擎的「自定义 Agent 列表」管理接口（按团队隔离）。
 *
 * <p>本服务不直接管理 Agent，仅做「微服务调用」编排，保持引擎为唯一权威数据源；
 * 透传 {@code X-Team-Id} 头，由引擎按团队隔离并做注入预检。
 * 引擎命中注入风险返回 400 时，错误体（含拒绝原因）原样透传给前端展示。
 */
@RestController
@RequestMapping("/api/agents")
public class CustomAgentController {

    private final EngineClient engine;

    public CustomAgentController(EngineClient engine) {
        this.engine = engine;
    }

    /** 自定义 Agent 列表（按团队隔离）。 */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> list(@RequestHeader(value = "X-Team-Id", required = false) String teamId) {
        return json(engine.get("/api/admin/agents", teamId));
    }

    /** 新增自定义 Agent（引擎侧含注入预检，命中返回 400 + 原因）。 */
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> add(@RequestHeader(value = "X-Team-Id", required = false) String teamId,
                                      @RequestBody Map<String, Object> body) {
        return json(engine.post("/api/admin/agents", body, teamId));
    }

    /** 编辑更新自定义 Agent（乐观锁 + 注入预检，冲突返回 409 / 注入返回 400）。 */
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> update(@PathVariable String id,
                                         @RequestHeader(value = "X-Team-Id", required = false) String teamId,
                                         @RequestBody Map<String, Object> body) {
        return json(engine.put("/api/admin/agents/" + id, body, teamId));
    }

    /** 删除自定义 Agent。 */
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> remove(@PathVariable String id,
                                         @RequestHeader(value = "X-Team-Id", required = false) String teamId) {
        return json(engine.delete("/api/admin/agents/" + id, teamId));
    }

    /** 启停自定义 Agent。 */
    @PostMapping(value = "/{id}/toggle", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> toggle(@PathVariable String id,
                                         @RequestHeader(value = "X-Team-Id", required = false) String teamId,
                                         @RequestBody(required = false) Map<String, Object> body) {
        return json(engine.post("/api/admin/agents/" + id + "/toggle",
                body == null ? Map.of() : body, teamId));
    }

    private ResponseEntity<String> json(String body) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(body);
    }
}
