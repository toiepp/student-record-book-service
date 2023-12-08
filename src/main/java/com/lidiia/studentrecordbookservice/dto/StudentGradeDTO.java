package com.lidiia.studentrecordbookservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentGradeDTO {
        private String subjectName;
        private String type;
        private String examinerName;
        private Integer grade;
        private LocalDateTime examDate;

        private TeacherDTO teacher;
        private SubjectRequestDTO subject;
}
