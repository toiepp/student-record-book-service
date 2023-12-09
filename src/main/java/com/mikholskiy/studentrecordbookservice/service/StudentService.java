package com.mikholskiy.studentrecordbookservice.service;

import com.mikholskiy.studentrecordbookservice.dto.StudentGradeDTO;
import com.mikholskiy.studentrecordbookservice.entity.AssessmentItem;
import com.mikholskiy.studentrecordbookservice.entity.Subject;
import com.mikholskiy.studentrecordbookservice.entity.User;
import com.mikholskiy.studentrecordbookservice.repository.AssessmentItemRepository;
import com.mikholskiy.studentrecordbookservice.repository.UserRepository;
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


    public List<StudentGradeDTO> getStudentGrades(Long studentId) {
        User student = userRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
        List<Subject> enrolledSubjects = student.getEnrolledSubjects();

        List<StudentGradeDTO> studentGrades = new ArrayList<>();
        for (Subject subject : enrolledSubjects) {
            List<AssessmentItem> assessments = assessmentItemRepository.findBySubjectAndStudent(subject, student);
            for (AssessmentItem assessment : assessments) {
                StudentGradeDTO gradeDTO = new StudentGradeDTO();
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
//    public List<StudentGradeDTO> getStudentGrades(Long studentId) {
//        User student = userRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
//        List<Subject> enrolledSubjects = student.getEnrolledSubjects();
//
//        List<StudentGradeDTO> studentGrades = new ArrayList<>();
//        LocalDateTime currentDateTime = LocalDateTime.now();
//
//        for (Subject subject : enrolledSubjects) {
//            List<AssessmentItem> exams = assessmentItemRepository.findBySubjectAndGradeIsNotNull(subject);
//
//            for (AssessmentItem assessment : exams) {
//                if (assessment.getExamDate() != null && assessment.getExamDate().isAfter(currentDateTime)) {
//                    // Проверяем, что дата экзамена в будущем
//                    StudentGradeDTO gradeDTO = new StudentGradeDTO();
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
