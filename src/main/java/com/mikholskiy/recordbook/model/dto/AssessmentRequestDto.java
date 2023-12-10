package com.mikholskiy.recordbook.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentRequestDto {
    private String type;
    private Integer grade;
    private Long subjectId;
    private Long teacherId;
    private Long studentId;
}
