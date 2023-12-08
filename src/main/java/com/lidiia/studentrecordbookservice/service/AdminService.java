package com.lidiia.studentrecordbookservice.service;

import com.lidiia.studentrecordbookservice.dto.AssessmentItemDTO;
import com.lidiia.studentrecordbookservice.dto.SubjectRequestDTO;
import com.lidiia.studentrecordbookservice.entity.AssessmentItem;
import com.lidiia.studentrecordbookservice.entity.Subject;
import com.lidiia.studentrecordbookservice.entity.User;
import com.lidiia.studentrecordbookservice.repository.AssessmentItemRepository;
import com.lidiia.studentrecordbookservice.repository.SubjectRepository;
import com.lidiia.studentrecordbookservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;
    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private AssessmentItemRepository assessmentItemRepository;

    public User updateUser(Long userId, User updatedUser) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setFatherName(updatedUser.getFatherName());
        existingUser.setPassword(updatedUser.getPassword());
        existingUser.setRole(updatedUser.getRole());

        return userRepository.save(existingUser);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }


    public Subject createSubject(SubjectRequestDTO subjectDTO) {
        Subject newSubject = new Subject();
        newSubject.setName(subjectDTO.getName());


        User teacher = userRepository.findById(subjectDTO.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        newSubject.assignTeacher(teacher);

        return subjectRepository.save(newSubject);
    }

    public Subject updateSubject(Long subjectId, SubjectRequestDTO subjectRequest) {
        Subject existingSubject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        existingSubject.setName(subjectRequest.getName());
        User teacher = userRepository.findById(subjectRequest.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        existingSubject.setTeacher(teacher);
        return subjectRepository.save(existingSubject);
    }

    public void deleteSubject(Long subjectId) {
        subjectRepository.deleteById(subjectId);
    }


    public AssessmentItem createAssessmentItem(AssessmentItemDTO assessmentItemDTO) {
        AssessmentItem newAssessmentItem = new AssessmentItem();
        newAssessmentItem.setType(assessmentItemDTO.getType());
        newAssessmentItem.setGrade(assessmentItemDTO.getGrade());
        newAssessmentItem.setExamDate(assessmentItemDTO.getExamDate());


        Subject subject = subjectRepository.findById(assessmentItemDTO.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        newAssessmentItem.setSubject(subject);


        User teacher = userRepository.findById(assessmentItemDTO.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        newAssessmentItem.setTeacher(teacher);

        String examinerName = createExaminerName(teacher.getFirstName(), teacher.getLastName(), teacher.getFatherName());
        newAssessmentItem.setExaminerName(examinerName);


        if (assessmentItemDTO.getStudentId() != null) {
            User student = userRepository.findById(assessmentItemDTO.getStudentId())
                    .orElseThrow(() -> new RuntimeException("Student not found"));
            newAssessmentItem.setStudent(student);
        }

        return assessmentItemRepository.save(newAssessmentItem);
    }


    public AssessmentItem updateAssessmentItem(Long assessmentItemId, AssessmentItemDTO updatedAssessmentItemDTO) {
        AssessmentItem existingAssessmentItem = assessmentItemRepository.findById(assessmentItemId)
                .orElseThrow(() -> new RuntimeException("Assessment item not found"));

        existingAssessmentItem.setType(updatedAssessmentItemDTO.getType());
        existingAssessmentItem.setExaminerName(updatedAssessmentItemDTO.getExaminerName());
        existingAssessmentItem.setGrade(updatedAssessmentItemDTO.getGrade());
        existingAssessmentItem.setExamDate(updatedAssessmentItemDTO.getExamDate());


        Subject subject = subjectRepository.findById(updatedAssessmentItemDTO.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        existingAssessmentItem.setSubject(subject);


        User teacher = userRepository.findById(updatedAssessmentItemDTO.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        existingAssessmentItem.setTeacher(teacher);

        String examinerName = createExaminerName(teacher.getFirstName(), teacher.getLastName(), teacher.getFatherName());
        existingAssessmentItem.setExaminerName(examinerName);


        if (updatedAssessmentItemDTO.getStudentId() != null) {
            User student = userRepository.findById(updatedAssessmentItemDTO.getStudentId())
                    .orElseThrow(() -> new RuntimeException("Student not found"));
            existingAssessmentItem.setStudent(student);
        }
        return assessmentItemRepository.save(existingAssessmentItem);
    }
    private String createExaminerName(String firstName, String lastName, String fatherName) {
        return firstName + " " + lastName + " " + fatherName;
    }


    public void deleteAssessmentItem(Long assessmentItemId) {
        assessmentItemRepository.deleteById(assessmentItemId);
    }



    public void addStudentToSubject(Long studentId, Long subjectId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        subject.addStudent(student);
        subjectRepository.save(subject);
    }

    public void removeStudentFromSubject(Long studentId, Long subjectId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        subject.removeStudent(student);
        subjectRepository.save(subject);
    }


    public void assignTeacherToSubject(Long teacherId, Long subjectId) {
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        subject.assignTeacher(teacher);
        subjectRepository.save(subject);
    }

    public void unassignTeacherFromSubject(Long subjectId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        subject.unassignTeacher();
        subjectRepository.save(subject);
    }

    public void approveUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));

        authService.approveUser(userEmail);

        userRepository.save(user);
    }

    public void rejectUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));

        userRepository.delete(user);
    }


}