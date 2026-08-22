package com.codereview.console.controller;

import com.codereview.console.EngineClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Skills 市场代理：将前端请求透传到代码审查引擎的技能管理接口（按团队隔离）。
 *
 * <p>本服务不直接管理技能，仅做「微服务调用」编排，保持引擎为唯一权威数据源。
 * 透传 {@code X-Team-Id} 头，由引擎按团队隔离技能 / 规则。
 */
@RestController
@RequestMapping("/api/skills")
public class SkillMarketController {

    private final EngineClient engine;

    public SkillMarketController(EngineClient engine) {
        this.engine = engine;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> list(@RequestHeader(value = "X-Team-Id", required = false) String teamId) {
        return json(engine.get("/api/admin/skills", teamId));
    }

    @PostMapping(value = "/{name}/toggle", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> toggle(@PathVariable String name,
                                        @RequestHeader(value = "X-Team-Id", required = false) String teamId,
                                        @RequestBody(required = false) Map<String, Object> body) {
        return json(engine.post("/api/admin/skills/" + name + "/toggle",
                body == null ? Map.of() : body, teamId));
    }

    @PostMapping(value = "/custom", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> add(@RequestHeader(value = "X-Team-Id", required = false) String teamId,
                                     @RequestBody Map<String, Object> body) {
        return json(engine.post("/api/admin/skills/custom", body, teamId));
    }

    @DeleteMapping(value = "/custom/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> remove(@PathVariable String id,
                                        @RequestHeader(value = "X-Team-Id", required = false) String teamId) {
        return json(engine.delete("/api/admin/skills/custom/" + id, teamId));
    }

    /**
     * 批量导入团队规则（YAML 低代码规则平台）。
     * 前端以原始文本提交 YAML，本代理以 text/plain 透传到引擎（携带 X-Team-Id）。
     */
    @PostMapping(value = "/yaml", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> importYaml(@RequestHeader(value = "X-Team-Id", required = false) String teamId,
                                            @RequestBody String yaml) {
        return json(engine.postRaw("/api/admin/skills/yaml", yaml, teamId));
    }

    private ResponseEntity<String> json(String body) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(body);
    }
}
