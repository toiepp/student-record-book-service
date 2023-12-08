package com.lidiia.studentrecordbookservice.rest;

import com.lidiia.studentrecordbookservice.dto.AssessmentItemDTO;
import com.lidiia.studentrecordbookservice.dto.SubjectRequestDTO;
import com.lidiia.studentrecordbookservice.entity.AssessmentItem;
import com.lidiia.studentrecordbookservice.entity.Subject;
import com.lidiia.studentrecordbookservice.entity.User;
import com.lidiia.studentrecordbookservice.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PutMapping("/users/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody User updatedUser) {
        User updated = adminService.updateUser(userId, updatedUser);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        adminService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/subjects")
    public ResponseEntity<Subject> createSubject(@RequestBody SubjectRequestDTO subjectDTO) {
        Subject created = adminService.createSubject(subjectDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/subjects/{subjectId}")
    public ResponseEntity<Subject> updateSubject(@PathVariable Long subjectId, @RequestBody SubjectRequestDTO subjectDTO) {
        Subject updated = adminService.updateSubject(subjectId, subjectDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/subjects/{subjectId}")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long subjectId) {
        adminService.deleteSubject(subjectId);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/assessmentItems")
    public ResponseEntity<AssessmentItem> createAssessmentItem(@RequestBody AssessmentItemDTO assessmentItemDTO) {
        AssessmentItem created = adminService.createAssessmentItem(assessmentItemDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }


    @PutMapping("/assessmentItems/{assessmentItemId}")
    public ResponseEntity<AssessmentItem> updateAssessmentItem(@PathVariable Long assessmentItemId, @RequestBody AssessmentItemDTO assessmentItemDTO) {
        AssessmentItem updated = adminService.updateAssessmentItem(assessmentItemId, assessmentItemDTO);
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


    @PostMapping("/subjects/{subjectId}/teachers/{teacherId}")
    public ResponseEntity<Void> assignTeacherToSubject(@PathVariable Long teacherId, @PathVariable Long subjectId) {
        adminService.assignTeacherToSubject(teacherId, subjectId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/subjects/{subjectId}/teachers")
    public ResponseEntity<Void> unassignTeacherFromSubject(@PathVariable Long subjectId) {
        adminService.unassignTeacherFromSubject(subjectId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/approveUser")
    public ResponseEntity<Void> approveUser(@RequestParam String userEmail) {
        adminService.approveUser(userEmail);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/rejectUser")
    public ResponseEntity<Void> rejectUser(@RequestParam String userEmail) {
        adminService.rejectUser(userEmail);
        return ResponseEntity.ok().build();
    }
}