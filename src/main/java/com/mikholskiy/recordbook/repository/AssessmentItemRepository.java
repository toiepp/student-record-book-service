package com.mikholskiy.recordbook.repository;

import com.mikholskiy.recordbook.model.AssessmentItem;
import com.mikholskiy.recordbook.model.Subject;
import com.mikholskiy.recordbook.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AssessmentItemRepository extends JpaRepository<AssessmentItem, Long> {

    List<AssessmentItem> findBySubjectAndTypeAndExamDateAfter(Subject subject, String exam, LocalDateTime currentDateTime);

    List<AssessmentItem> findBySubjectAndStudent(Subject subject, User student);
}