package com.mikholskiy.recordbook.service;

import com.mikholskiy.recordbook.dto.AssessmentItemDto;
import com.mikholskiy.recordbook.dto.SubjectDto;
import com.mikholskiy.recordbook.dto.SubjectRequestDto;
import com.mikholskiy.recordbook.dto.UserDto;
import com.mikholskiy.recordbook.entity.AssessmentItem;
import com.mikholskiy.recordbook.entity.Subject;
import com.mikholskiy.recordbook.entity.User;
import com.mikholskiy.recordbook.repository.AssessmentItemRepository;
import com.mikholskiy.recordbook.repository.SubjectRepository;
import com.mikholskiy.recordbook.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.StringJoiner;
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

    public AssessmentItem createAssessmentItem(AssessmentItemDto assessmentItemDTO) {
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


    public AssessmentItem updateAssessmentItem(Long assessmentItemId, AssessmentItemDto updatedAssessmentItemDTO) {
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