package com.codereview.console;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 代码审查系统 · 管理控制台。
 *
 * <p>定位：独立微服务（:8081），负责两件事：
 * <ol>
 *   <li>托管 Vue 前端静态资源（Skills 市场 / 团队知识上传 / 仪表盘）；</li>
 *   <li>通过 WebClient 以「微服务调用」方式访问代码审查引擎（:8080）的管理接口，
 *       将技能与知识能力透传给前端。</li>
 * </ol>
 * 前端只对接本服务，引擎地址通过 {@code engine.base-url} 配置，可随时切换。
 */
@SpringBootApplication
public class ConsoleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsoleApplication.class, args);
    }
}
