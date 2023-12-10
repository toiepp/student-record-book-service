package com.mikholskiy.recordbook.controller;

import com.mikholskiy.recordbook.model.dto.AssessmentItemDto;
import com.mikholskiy.recordbook.model.dto.GradeRequest;
import com.mikholskiy.recordbook.model.dto.SubjectRequestDto;
import com.mikholskiy.recordbook.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Secured({"ADMIN", "TEACHER"})
@RequestMapping("/api/teacher")
public class TeacherController {
    TeacherService teacherService;

    @Autowired
    public TeacherController setTeacherService(TeacherService teacherService) {
        this.teacherService = teacherService;
        return this;
    }

    @GetMapping("/subjects/{teacherId}")
    public ResponseEntity<List<SubjectRequestDto>> getSubjectsByTeacher(@PathVariable Long teacherId) {
        var subjects = teacherService.getSubjectsByTeacher(teacherId);
        return ResponseEntity.ok(subjects);
    }


    @PostMapping("/assessmentItems/{teacherId}/{subjectId}/{studentId}")
    public ResponseEntity<AssessmentItemDto> createAssessmentItem(
            @PathVariable Long teacherId,
            @PathVariable Long subjectId,
            @PathVariable Long studentId,
            @RequestBody GradeRequest gradeRequest
            ) {
        var assessmentItemDto = teacherService.createAssessmentItem(teacherId, subjectId, studentId, gradeRequest);
        return ResponseEntity.ok(assessmentItemDto);
    }
}
