package com.lidiia.studentrecordbookservice;

import com.lidiia.studentrecordbookservice.config.AuthConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(AuthConfig.class)
public class StudentRecordBookServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentRecordBookServiceApplication.class, args);
    }

}
