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
    private TeacherService teacherService;

    @Autowired
    public TeacherController setTeacherService(TeacherService teacherService) {
        this.teacherService = teacherService;
        return this;
    }

    @GetMapping("/{teacherId}/subjects")
    public ResponseEntity<List<SubjectRequestDto>> getSubjectsByTeacher(@PathVariable Long teacherId) {
        var subjects = teacherService.getSubjectsByTeacher(teacherId);
        return ResponseEntity.ok(subjects);
    }


    @PostMapping("/{teacherId}/assessmentItems")
    public ResponseEntity<AssessmentItemDto> createAssessmentItem(
            @PathVariable Long teacherId,
            @RequestParam Long subjectId,
            @RequestParam Long studentId,
            @RequestBody GradeRequest gradeRequest
    ) {
        var assessmentItemDto = teacherService.createAssessmentItem(teacherId, subjectId, studentId, gradeRequest);
        return ResponseEntity.ok(assessmentItemDto);
    }
}
