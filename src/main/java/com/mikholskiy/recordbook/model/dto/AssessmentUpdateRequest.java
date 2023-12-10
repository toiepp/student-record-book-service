package com.mikholskiy.recordbook.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssessmentUpdateRequest {
    private Long teacherId;
    private Integer grade;
}
