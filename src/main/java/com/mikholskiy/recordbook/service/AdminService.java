package com.mikholskiy.recordbook.service;

import com.mikholskiy.recordbook.model.AssessmentItem;
import com.mikholskiy.recordbook.model.Subject;
import com.mikholskiy.recordbook.model.User;
import com.mikholskiy.recordbook.model.dto.*;
import com.mikholskiy.recordbook.repository.AssessmentItemRepository;
import com.mikholskiy.recordbook.repository.SubjectRepository;
import com.mikholskiy.recordbook.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {
    private UserRepository userRepository;
    private AuthService authService;
    private SubjectRepository subjectRepository;
    private AssessmentItemRepository assessmentItemRepository;


    @Autowired
    public AdminService setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
        return this;
    }

    @Autowired
    public AdminService setAuthService(AuthService authService) {
        this.authService = authService;
        return this;
    }

    @Autowired
    public AdminService setSubjectRepository(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
        return this;
    }

    @Autowired
    public AdminService setAssessmentItemRepository(AssessmentItemRepository assessmentItemRepository) {
        this.assessmentItemRepository = assessmentItemRepository;
        return this;
    }

    public List<UserDto> findAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserDto(user.getId(), user.getEmail(), user.getLastName(), user.getFirstName(), user.getRole().name()))
                .sorted((o1, o2) -> (int) (o1.getId() - o2.getId()))
                .collect(Collectors.toList());
    }


/*
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
*/

    public void deleteUser(Long userId) {
        var user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("user does not exists"));
        user.getEnrolledSubjects().forEach(subject -> subject.removeStudent(user));
        user.getEnrolledSubjects().clear();
        var assessments = assessmentItemRepository.findAll();
        assessments.stream()
                .filter(assessmentItem -> assessmentItem.getStudent().getId().equals(user.getId()))
                .forEach(assessmentItem -> assessmentItem.setStudent(null));
        userRepository.delete(user);
    }

    public List<SubjectDto> findAllSubject() {
        List<Subject> subjects = subjectRepository.findAll();
        return subjects.stream()
                .map(subject -> {
                    var teacher = userRepository.findById(subject.getTeacher().getId())
                            .orElseThrow(() -> new EntityNotFoundException("No such user"));

                    return SubjectDto.from(subject, teacher);
                }).collect(Collectors.toList());
    }

    public SubjectDto createSubject(SubjectRequestDto subjectDTO) {
        Subject newSubject = new Subject();
        newSubject.setName(subjectDTO.getName());


        User teacher = userRepository.findById(subjectDTO.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        newSubject.assignTeacher(teacher);

        var subject = subjectRepository.save(newSubject);

        return SubjectDto.from(subject, teacher);
    }

    public SubjectDto updateSubject(Long subjectId, SubjectRequestDto subjectRequest) {
        Subject existingSubject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        existingSubject.setName(subjectRequest.getName());
        User teacher = userRepository.findById(subjectRequest.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        existingSubject.setTeacher(teacher);
        var saved = subjectRepository.save(existingSubject);

        return SubjectDto.from(saved, teacher);
    }

    public void deleteSubject(Long subjectId) {
        subjectRepository.deleteById(subjectId);
    }


    public List<AssessmentItemDto> findAllAssessments() {
        List<AssessmentItem> assessmentItems = assessmentItemRepository.findAll();
        return assessmentItems.stream()
                .map(assessmentItem -> new AssessmentItemDto(
                        assessmentItem.getType(),
                        assessmentItem.getExaminerName(),
                        assessmentItem.getGrade(),
                        assessmentItem.getExamDate(),
                        assessmentItem.getSubject().getId(),
                        assessmentItem.getTeacher().getId(),
                        assessmentItem.getStudent().getId()
                )).collect(Collectors.toList());
    }

    public AssessmentItem createAssessmentItem(AssessmentRequestDto assessmentRequestDto) {
        AssessmentItem newAssessmentItem = new AssessmentItem();
        newAssessmentItem.setType(assessmentRequestDto.getType());
        newAssessmentItem.setGrade(assessmentRequestDto.getGrade());
        newAssessmentItem.setExamDate(LocalDateTime.now());


        Subject subject = subjectRepository.findById(assessmentRequestDto.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        newAssessmentItem.setSubject(subject);


        User teacher = userRepository.findById(assessmentRequestDto.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        newAssessmentItem.setTeacher(teacher);

        String examinerName = createExaminerName(teacher.getFirstName(), teacher.getLastName(), teacher.getFatherName());
        newAssessmentItem.setExaminerName(examinerName);


        if (assessmentRequestDto.getStudentId() != null) {
            User student = userRepository.findById(assessmentRequestDto.getStudentId())
                    .orElseThrow(() -> new RuntimeException("Student not found"));
            newAssessmentItem.setStudent(student);
        }

        var save = assessmentItemRepository.save(newAssessmentItem);

        return save;
    }


    public AssessmentItem updateAssessmentItem(Long assessmentItemId, AssessmentUpdateRequest assessmentUpdateRequest) {
        AssessmentItem existingAssessmentItem = assessmentItemRepository.findById(assessmentItemId)
                .orElseThrow(() -> new RuntimeException("Assessment item not found"));

        // обновить можно только оценку и преподавателя

        existingAssessmentItem.setGrade(assessmentUpdateRequest.getGrade());
        existingAssessmentItem.setExamDate(LocalDateTime.now());

        if (assessmentUpdateRequest.getTeacherId() != null) {
            User teacher = userRepository.findById(assessmentUpdateRequest.getTeacherId())
                    .orElseThrow(() -> new RuntimeException("Teacher not found"));
            existingAssessmentItem.setTeacher(teacher);

            String examinerName = createExaminerName(teacher.getFirstName(), teacher.getLastName(), teacher.getFatherName());
            existingAssessmentItem.setExaminerName(examinerName);
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