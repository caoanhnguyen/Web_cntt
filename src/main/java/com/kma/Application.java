package com.kma;

import com.google.api.client.util.Value;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;
import java.util.TimeZone;

@SpringBootApplication
public class Application {
    static {
        // Load file `.env` từ thư mục hệ thống
        Dotenv dotenv = Dotenv.configure()
                .directory(Paths.get("").toAbsolutePath().toString()) // Load từ thư mục hiện tại
                .ignoreIfMissing() // Không báo lỗi nếu thiếu
                .load();

        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
    }
    public static void main(String[] args) {

        // Set timezone mặc định là UTC
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SpringApplication.run(Application.class, args);

    }

    @Component
     class RedisConfigChecker implements CommandLineRunner {

        @Value("${spring.redis.host}")
        private String redisHost;

        @Value("${spring.redis.port}")
        private String redisPort;

        @Override
        public void run(String... args) throws Exception {
            System.out.println("✅ Redis Host: " + redisHost);
            System.out.println("✅ Redis Port: " + redisPort);
        }
    }
}