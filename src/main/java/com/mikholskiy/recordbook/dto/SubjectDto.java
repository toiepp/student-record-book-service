package com.mikholskiy.recordbook.dto;

import com.mikholskiy.recordbook.entity.Subject;
import com.mikholskiy.recordbook.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.StringJoiner;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubjectDto {
    private Long id;
    private String name;
    private String teacherName;

    public static SubjectDto from (Subject subject, User teacher) {
        return new SubjectDto(
                subject.getId(),
                subject.getName(),
                new StringJoiner(" ")
                        .add(teacher.getLastName())
                        .add(teacher.getFirstName().charAt(0) + ".")
                        .add((teacher.getFatherName() != null) ? teacher.getFatherName().charAt(0) + "." : "")
                        .toString().trim()
        );
    }
}
