package com.lidiia.studentrecordbookservice.rest;

import com.lidiia.studentrecordbookservice.dto.StudentGradeDTO;
import com.lidiia.studentrecordbookservice.entity.AssessmentItem;
import com.lidiia.studentrecordbookservice.service.JwtService;
import com.lidiia.studentrecordbookservice.service.StudentService;
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
