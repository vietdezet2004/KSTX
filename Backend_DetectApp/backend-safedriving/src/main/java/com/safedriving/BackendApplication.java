package com.safedriving;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

@Slf4j
@SpringBootApplication
public class BackendApplication {

	private final Environment env;

	public BackendApplication(Environment env) {
		this.env = env;
	}

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void onReady() {
		String port = env.getProperty("server.port", "8080");
		String swaggerPath = env.getProperty("springdoc.swagger-ui.path", "/swagger-ui.html");
		log.info("====================================================================");
		log.info("🚀 Ứng dụng đã sẵn sàng!");
		log.info("👉 Local URL:    http://localhost:{}", port);
		log.info("👉 Swagger UI:   http://localhost:{}{}", port, swaggerPath);
		log.info("👉 API Docs:     http://localhost:{}/api-docs", port);
		log.info("====================================================================");
	}

}
