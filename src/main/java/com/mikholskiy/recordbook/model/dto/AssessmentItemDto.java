package com.mikholskiy.recordbook.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssessmentItemDto {
    private String type;
    private String examinerName;
    private Integer grade;
    private LocalDateTime examDate;
    private Long subjectId;
    private Long teacherId;
    private Long studentId;
}
