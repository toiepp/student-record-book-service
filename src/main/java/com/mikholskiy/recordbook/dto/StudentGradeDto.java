package com.mikholskiy.recordbook.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentGradeDto {
        private String subjectName;
        private String type;
        private String examinerName;
        private Integer grade;
        private LocalDateTime examDate;

        private TeacherDto teacher;
        private SubjectRequestDto subject;
}
