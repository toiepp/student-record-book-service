package com.mikholskiy.studentrecordbookservice.rest;

import com.mikholskiy.studentrecordbookservice.entity.AssessmentItem;
import com.mikholskiy.studentrecordbookservice.entity.Subject;
import com.mikholskiy.studentrecordbookservice.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

    @Autowired
    TeacherService teacherService;

    @GetMapping("/subjects/{teacherId}")
    public ResponseEntity<List<Subject>> getSubjectsByTeacher(@PathVariable Long teacherId) {
        List<Subject> subjects = teacherService.getSubjectsByTeacher(teacherId);
        return ResponseEntity.ok(subjects);
    }


    @PostMapping("/assessmentItems/{teacherId}/{subjectId}/{studentId}")
    public ResponseEntity<AssessmentItem> createAssessmentItem(
            @PathVariable Long teacherId,
            @PathVariable Long subjectId,
            @PathVariable Long studentId,
            @RequestBody AssessmentItem assessmentItem
    ) {
       AssessmentItem assessmentItem1 = teacherService.createAssessmentItem(teacherId,subjectId,studentId,assessmentItem);
        return ResponseEntity.ok(assessmentItem1);
    }
}
