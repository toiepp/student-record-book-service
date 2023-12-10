package com.mikholskiy.recordbook.repository;

import com.mikholskiy.recordbook.model.Subject;
import com.mikholskiy.recordbook.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findByTeacher(User teacher);
}