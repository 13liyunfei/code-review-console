package com.codereview.console.controller;

import com.codereview.console.EngineClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 团队知识代理：透传规范文档 / 操作手册 / 视频的上传、列表与删除到引擎（按团队隔离）。
 */
@RestController
@RequestMapping("/api/knowledge")
public class KnowledgeController {

    private final EngineClient engine;

    public KnowledgeController(EngineClient engine) {
        this.engine = engine;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> list(@RequestHeader(value = "X-Team-Id", required = false) String teamId) {
        return json(engine.get("/api/admin/knowledge", teamId));
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> remove(@PathVariable String id,
                                        @RequestHeader(value = "X-Team-Id", required = false) String teamId) {
        return json(engine.delete("/api/admin/knowledge/" + id, teamId));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> upload(@RequestHeader(value = "X-Team-Id", required = false) String teamId,
                                        @RequestParam(value = "file", required = false) MultipartFile file,
                                        @RequestParam(value = "source", required = false) String source,
                                        @RequestParam(value = "category", required = false) String category,
                                        @RequestParam(value = "type", required = false, defaultValue = "document") String type,
                                        @RequestParam(value = "text", required = false) String text) {
        Map<String, String> form = new LinkedHashMap<>();
        if (source != null) {
            form.put("source", source);
        }
        if (category != null) {
            form.put("category", category);
        }
        form.put("type", type);
        if (text != null) {
            form.put("text", text);
        }
        return json(engine.upload("/api/admin/knowledge", file, form, teamId));
    }

    private ResponseEntity<String> json(String body) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(body);
    }
}
