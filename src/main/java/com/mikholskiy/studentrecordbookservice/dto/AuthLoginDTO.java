package com.mikholskiy.studentrecordbookservice.dto;

import com.mikholskiy.studentrecordbookservice.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthLoginDTO {
    private String email;
    private String password;
    private UserRole userRole;
}
