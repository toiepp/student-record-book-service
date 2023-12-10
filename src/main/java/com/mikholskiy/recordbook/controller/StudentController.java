package com.mikholskiy.recordbook.controller;

import com.mikholskiy.recordbook.model.dto.StudentGradeDto;
import com.mikholskiy.recordbook.service.JwtService;
import com.mikholskiy.recordbook.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Secured({"ADMIN", "STUDENT"})
@RequestMapping("/api/student")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @Autowired
    JwtService jwtService;

    @GetMapping("/grades/{studentId}")
    public ResponseEntity<List<StudentGradeDto>> getStudentGrades(@PathVariable Long studentId) {
        List<StudentGradeDto> studentGrades = studentService.getStudentGrades(studentId);
        return ResponseEntity.ok(studentGrades);
    }
}
