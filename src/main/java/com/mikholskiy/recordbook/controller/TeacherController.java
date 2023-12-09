package com.mikholskiy.recordbook.controller;

import com.mikholskiy.recordbook.dto.AssessmentItemDto;
import com.mikholskiy.recordbook.dto.SubjectRequestDto;
import com.mikholskiy.recordbook.entity.AssessmentItem;
import com.mikholskiy.recordbook.entity.Subject;
import com.mikholskiy.recordbook.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
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
            @RequestBody AssessmentItem assessmentItem
    ) {
       var assessmentItemDto = teacherService.createAssessmentItem(teacherId,subjectId,studentId,assessmentItem);
        return ResponseEntity.ok(assessmentItemDto);
    }
}
