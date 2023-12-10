package com.mikholskiy.recordbook.model.dto;

import com.mikholskiy.recordbook.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String fatherName;
    private UserRole userRole;
}
