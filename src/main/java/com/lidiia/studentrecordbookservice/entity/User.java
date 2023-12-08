package com.lidiia.studentrecordbookservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "app_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String fatherName;
    private String email;
    private String password;
    @Column(name = "is_approved", nullable = false, columnDefinition = "boolean default false")
    private Boolean isApproved;
    private LocalDateTime createdDate;

    @Enumerated(EnumType.STRING)

    private UserRole role;

    @ManyToMany(mappedBy = "students")
    @JsonIgnoreProperties("enrolledSubjects")
    private List<Subject> enrolledSubjects = new ArrayList<>();


}
