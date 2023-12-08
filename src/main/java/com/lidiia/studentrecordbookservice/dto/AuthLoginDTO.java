package com.lidiia.studentrecordbookservice.dto;

import com.lidiia.studentrecordbookservice.entity.UserRole;
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
