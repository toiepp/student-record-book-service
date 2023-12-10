package com.mikholskiy.recordbook.controller;

import com.mikholskiy.recordbook.model.dto.*;
import com.mikholskiy.recordbook.model.AssessmentItem;
import com.mikholskiy.recordbook.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Secured({"ADMIN"})
@RequestMapping("/api/admin")
public class AdminController {
    private AdminService adminService;

    @Autowired
    public AdminController setAdminService(AdminService adminService) {
        this.adminService = adminService;
        return this;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(adminService.findAllUsers());
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        adminService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/subjects")
    public ResponseEntity<List<SubjectDto>> getAllSubjects() {
        return ResponseEntity.ok(adminService.findAllSubject());
    }

    @PostMapping("/subjects")
    public ResponseEntity<SubjectDto> createSubject(@RequestBody SubjectRequestDto subjectDTO) {
        var created = adminService.createSubject(subjectDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/subjects/{subjectId}")
    public ResponseEntity<SubjectDto> updateSubject(@PathVariable Long subjectId,
                                                 @RequestBody SubjectRequestDto subjectDTO) {
        SubjectDto updated = adminService.updateSubject(subjectId, subjectDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/subjects/{subjectId}")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long subjectId) {
        adminService.deleteSubject(subjectId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/assessmentItems")
    public ResponseEntity<List<AssessmentItemDto>> findAllAssessments() {
        var assessments = adminService.findAllAssessments();
        return ResponseEntity.ok(assessments);
    }

    @PostMapping("/assessmentItems")
    public ResponseEntity<AssessmentItem> createAssessmentItem(@RequestBody AssessmentRequestDto assessmentItemDTO) {
        adminService.createAssessmentItem(assessmentItemDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PutMapping("/assessmentItems/{assessmentItemId}")
    public ResponseEntity<AssessmentItem> updateAssessmentItem(@PathVariable Long assessmentItemId,
                                                               @RequestBody AssessmentUpdateRequest assessmentUpdateRequest) {
        AssessmentItem updated = adminService.updateAssessmentItem(assessmentItemId, assessmentUpdateRequest);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/assessmentItems/{assessmentItemId}")
    public ResponseEntity<Void> deleteAssessmentItem(@PathVariable Long assessmentItemId) {
        adminService.deleteAssessmentItem(assessmentItemId);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/subjects/{subjectId}/students/{studentId}")
    public ResponseEntity<Void> addStudentToSubject(@PathVariable Long studentId, @PathVariable Long subjectId) {
        adminService.addStudentToSubject(studentId, subjectId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/subjects/{subjectId}/students/{studentId}")
    public ResponseEntity<Void> removeStudentFromSubject(@PathVariable Long studentId, @PathVariable Long subjectId) {
        adminService.removeStudentFromSubject(studentId, subjectId);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/subjects/{subjectId}/teachers")
    public ResponseEntity<Void> assignTeacherToSubject(@PathVariable Long subjectId, @RequestParam Long teacherId) {
        adminService.assignTeacherToSubject(teacherId, subjectId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/approveUser")
    public ResponseEntity<Void> approveUser(@RequestParam("email") String userEmail) {
        adminService.approveUser(userEmail);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/rejectUser")
    public ResponseEntity<Void> rejectUser(@RequestParam("email") String userEmail) {
        adminService.rejectUser(userEmail);
        return ResponseEntity.ok().build();
    }
}