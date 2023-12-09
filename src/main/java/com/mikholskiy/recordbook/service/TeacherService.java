package com.mikholskiy.recordbook.service;

import com.mikholskiy.recordbook.entity.AssessmentItem;
import com.mikholskiy.recordbook.entity.Subject;
import com.mikholskiy.recordbook.entity.User;
import com.mikholskiy.recordbook.repository.AssessmentItemRepository;
import com.mikholskiy.recordbook.repository.SubjectRepository;
import com.mikholskiy.recordbook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private AssessmentItemRepository assessmentItemRepository;

    public List<Subject> getSubjectsByTeacher(Long teacherId) {
        User teacher = userRepository.findById(teacherId).orElseThrow(() -> new RuntimeException("Teacher not found"));
        return subjectRepository.findByTeacher(teacher);
    }

    public AssessmentItem createAssessmentItem(Long teacherId, Long subjectId, Long studentId, AssessmentItem assessmentItem) {
        User teacher = userRepository.findById(teacherId).orElseThrow(() -> new RuntimeException("Teacher not found"));
        Subject subject = subjectRepository.findById(subjectId).orElseThrow(() -> new RuntimeException("Subject not found"));
        User student = userRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));

        if (subject.getTeacher().equals(teacher) && subject.getStudents().contains(student)) {
            assessmentItem.setSubject(subject);
            assessmentItem.setStudent(student);
            return assessmentItemRepository.save(assessmentItem);
        } else {
            throw new RuntimeException("Teacher is not assigned to the specified subject or student is not enrolled");
        }
    }
}
