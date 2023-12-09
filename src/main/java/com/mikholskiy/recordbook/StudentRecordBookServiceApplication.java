package com.mikholskiy.recordbook;

import com.mikholskiy.recordbook.config.AuthConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@Import(AuthConfig.class)
public class StudentRecordBookServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentRecordBookServiceApplication.class, args);
    }
}
