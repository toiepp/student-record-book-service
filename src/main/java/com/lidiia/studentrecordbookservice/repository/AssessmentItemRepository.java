package com.lidiia.studentrecordbookservice.repository;

import com.lidiia.studentrecordbookservice.entity.AssessmentItem;
import com.lidiia.studentrecordbookservice.entity.Subject;
import com.lidiia.studentrecordbookservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AssessmentItemRepository extends JpaRepository<AssessmentItem, Long> {

    List<AssessmentItem> findBySubjectAndTypeAndExamDateAfter(Subject subject, String exam, LocalDateTime currentDateTime);



    List<AssessmentItem> findBySubjectAndStudent(Subject subject, User student);
}