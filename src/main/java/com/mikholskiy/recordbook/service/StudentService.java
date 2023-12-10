package com.mikholskiy.recordbook.service;

import com.mikholskiy.recordbook.model.dto.StudentGradeDto;
import com.mikholskiy.recordbook.model.AssessmentItem;
import com.mikholskiy.recordbook.model.Subject;
import com.mikholskiy.recordbook.model.User;
import com.mikholskiy.recordbook.repository.AssessmentItemRepository;
import com.mikholskiy.recordbook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AssessmentItemRepository assessmentItemRepository;


    public List<StudentGradeDto> getStudentGrades(Long studentId) {
        User student = userRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
        List<Subject> enrolledSubjects = student.getEnrolledSubjects();

        List<StudentGradeDto> studentGrades = new ArrayList<>();
        for (Subject subject : enrolledSubjects) {
            List<AssessmentItem> assessments = assessmentItemRepository.findBySubjectAndStudent(subject, student);
            for (AssessmentItem assessment : assessments) {
                StudentGradeDto gradeDTO = new StudentGradeDto();
                gradeDTO.setSubjectName(subject.getName());
                gradeDTO.setType(assessment.getType());
                gradeDTO.setExaminerName(assessment.getExaminerName());
                gradeDTO.setGrade(assessment.getGrade());
                gradeDTO.setExamDate(assessment.getExamDate());

                studentGrades.add(gradeDTO);
            }
        }

        return studentGrades;
    }

//
//    public List<StudentGradeDto> getStudentGrades(Long studentId) {
//        User student = userRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
//        List<Subject> enrolledSubjects = student.getEnrolledSubjects();
//
//        List<StudentGradeDto> studentGrades = new ArrayList<>();
//        LocalDateTime currentDateTime = LocalDateTime.now();
//
//        for (Subject subject : enrolledSubjects) {
//            List<AssessmentItem> exams = assessmentItemRepository.findBySubjectAndGradeIsNotNull(subject);
//
//            for (AssessmentItem assessment : exams) {
//                if (assessment.getExamDate() != null && assessment.getExamDate().isAfter(currentDateTime)) {
//                    // Проверяем, что дата экзамена в будущем
//                    StudentGradeDto gradeDTO = new StudentGradeDto();
//                    gradeDTO.setSubjectName(subject.getName());
//                    gradeDTO.setType(assessment.getType());
//                    gradeDTO.setExaminerName(assessment.getExaminerName());
//                    gradeDTO.setGrade(assessment.getGrade());
//                    gradeDTO.setExamDate(assessment.getExamDate());
//
//                    studentGrades.add(gradeDTO);
//                }
//            }
//        }
//
//        return studentGrades;
//    }


    public List<AssessmentItem> getUpcomingExamsForStudent(Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        List<AssessmentItem> upcomingExams = new ArrayList<>();
        LocalDateTime currentDateTime = LocalDateTime.now();

        for (Subject subject : student.getEnrolledSubjects()) {
            List<AssessmentItem> exams = assessmentItemRepository.findBySubjectAndTypeAndExamDateAfter(
                    subject, "Exam", currentDateTime);
            upcomingExams.addAll(exams);
        }

        return upcomingExams;
    }
}
