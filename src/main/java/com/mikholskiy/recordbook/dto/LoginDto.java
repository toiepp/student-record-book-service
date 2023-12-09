package com.mikholskiy.recordbook.dto;

import com.mikholskiy.recordbook.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String fatherName;
    private UserRole userRole;
}
