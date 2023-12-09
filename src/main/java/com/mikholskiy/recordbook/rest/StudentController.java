package com.mikholskiy.recordbook.rest;

import com.mikholskiy.recordbook.dto.StudentGradeDTO;
import com.mikholskiy.recordbook.entity.AssessmentItem;
import com.mikholskiy.recordbook.service.JwtService;
import com.mikholskiy.recordbook.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @Autowired
    JwtService jwtService;

    @GetMapping("/grades/{studentId}")
    public ResponseEntity<List<StudentGradeDTO>> getStudentGrades(@PathVariable Long studentId) {

        List<StudentGradeDTO> studentGrades = studentService.getStudentGrades(studentId);
        return ResponseEntity.ok(studentGrades);
    }
    @GetMapping("/upcoming-exams/{studentId}")
    public ResponseEntity<List<AssessmentItem>> getUpcomingExams(@PathVariable Long studentId) {
        List<AssessmentItem> upcomingExams = studentService.getUpcomingExamsForStudent(studentId);
        return ResponseEntity.ok(upcomingExams);
    }
}
