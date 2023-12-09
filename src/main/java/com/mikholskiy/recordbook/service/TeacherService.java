package com.mikholskiy.recordbook.service;

import com.mikholskiy.recordbook.dto.AssessmentItemDto;
import com.mikholskiy.recordbook.dto.SubjectRequestDto;
import com.mikholskiy.recordbook.entity.AssessmentItem;
import com.mikholskiy.recordbook.entity.Subject;
import com.mikholskiy.recordbook.entity.User;
import com.mikholskiy.recordbook.repository.AssessmentItemRepository;
import com.mikholskiy.recordbook.repository.SubjectRepository;
import com.mikholskiy.recordbook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
//        return subjectRepository.findByTeacher(teacher);
        return teacher.getEnrolledSubjects().stream()
                .map(subject -> new SubjectRequestDto(subject.getName(), subject.getTeacher().getId()))
                .collect(Collectors.toList());
    }

    public AssessmentItemDto createAssessmentItem(Long teacherId, Long subjectId, Long studentId, AssessmentItem assessmentItem) {
        User teacher = userRepository.findById(teacherId).orElseThrow(() -> new RuntimeException("Teacher not found"));
        Subject subject = subjectRepository.findById(subjectId).orElseThrow(() -> new RuntimeException("Subject not found"));
        User student = userRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));

        if (subject.getTeacher().equals(teacher) && subject.getStudents().contains(student)) {
            assessmentItem.setSubject(subject);
            assessmentItem.setStudent(student);
            var assessment = assessmentItemRepository.save(assessmentItem);

            AssessmentItemDto assessmentItemDto =
                    new AssessmentItemDto(assessment.getType(),
                            assessment.getExaminerName(),
                            assessment.getGrade(),
                            assessment.getExamDate(),
                            assessment.getSubject().getId(),
                            assessment.getTeacher().getId(),
                            assessment.getStudent().getId());

            return assessmentItemDto;

        } else {
            throw new RuntimeException("Teacher is not assigned to the specified subject or student is not enrolled");
        }
    }
}
