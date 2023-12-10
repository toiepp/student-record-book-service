package com.mikholskiy.recordbook.service;

import com.mikholskiy.recordbook.dto.AssessmentItemDto;
import com.mikholskiy.recordbook.dto.GradeRequest;
import com.mikholskiy.recordbook.dto.StudentDto;
import com.mikholskiy.recordbook.dto.SubjectRequestDto;
import com.mikholskiy.recordbook.entity.AssessmentItem;
import com.mikholskiy.recordbook.entity.Subject;
import com.mikholskiy.recordbook.entity.User;
import com.mikholskiy.recordbook.repository.AssessmentItemRepository;
import com.mikholskiy.recordbook.repository.SubjectRepository;
import com.mikholskiy.recordbook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Service
public class TeacherService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private AssessmentItemRepository assessmentItemRepository;

    public List<SubjectRequestDto> getSubjectsByTeacher(Long teacherId) {
        User teacher = userRepository.findById(teacherId).orElseThrow(() -> new RuntimeException("Teacher not found"));

        var subjects = subjectRepository.findByTeacher(teacher);

        return subjects.stream()
                .map(subject -> {
                    var studentDtos = subject.getStudents().stream()
                            .map(student -> new StudentDto(student.getFirstName(), student.getLastName(), student.getFatherName(), student.getId()))
                            .collect(Collectors.toList());
                    return new SubjectRequestDto(subject.getName(),
                            subject.getTeacher().getId(), studentDtos);
                }).collect(Collectors.toList());
    }

    public AssessmentItemDto createAssessmentItem(Long teacherId, Long subjectId, Long studentId, GradeRequest gradeRequest) {
        User teacher = userRepository.findById(teacherId).orElseThrow(() -> new RuntimeException("Teacher not found"));
        Subject subject = subjectRepository.findById(subjectId).orElseThrow(() -> new RuntimeException("Subject not found"));
        User student = userRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));


        if (subject.getTeacher().equals(teacher) && subject.getStudents().contains(student)) {
            AssessmentItem assessmentItem = new AssessmentItem();
            assessmentItem.setTeacher(teacher);
            assessmentItem.setSubject(subject);
            assessmentItem.setStudent(student);
            assessmentItem.setGrade(gradeRequest.getGrade());
            assessmentItem.setType(gradeRequest.getType());
            assessmentItem.setExamDate(LocalDateTime.now());
            assessmentItem.setExaminerName(
                    teacher.getLastName() + " " +
                            teacher.getFirstName().charAt(0) + "." +
                            ((teacher.getFatherName() != null) ? teacher.getFatherName().charAt(0) + "." : "")
            );

            var assessment = assessmentItemRepository.save(assessmentItem);

            return new AssessmentItemDto(assessment.getType(),
                    assessment.getExaminerName(),
                    assessment.getGrade(),
                    assessment.getExamDate(),
                    assessment.getSubject().getId(),
                    assessment.getTeacher().getId(),
                    assessment.getStudent().getId());

        } else {
            throw new RuntimeException("Teacher is not assigned to the specified subject or student is not enrolled");
        }
    }
}
