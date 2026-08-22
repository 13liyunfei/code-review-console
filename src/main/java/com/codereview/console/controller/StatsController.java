package com.codereview.console.controller;

import com.codereview.console.EngineClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 仪表盘统计代理：透传引擎的技能与知识概况（按团队隔离）。
 */
@RestController
@RequestMapping("/api/stats")
public class StatsController {

    private final EngineClient engine;

    public StatsController(EngineClient engine) {
        this.engine = engine;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> stats(@RequestHeader(value = "X-Team-Id", required = false) String teamId) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(engine.get("/api/admin/stats", teamId));
    }
}
