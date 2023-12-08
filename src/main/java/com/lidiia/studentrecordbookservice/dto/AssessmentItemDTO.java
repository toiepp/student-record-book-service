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
public class AssessmentItemDTO {
    private String type;
    private String examinerName;
    private Integer grade;
    private LocalDateTime examDate;
    private Long subjectId;
    private Long teacherId;
    private Long studentId;

}
