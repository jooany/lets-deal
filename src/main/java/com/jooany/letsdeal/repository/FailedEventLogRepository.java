package com.jooany.letsdeal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jooany.letsdeal.model.entity.FailedEventLog;

@Repository
public interface FailedEventLogRepository extends JpaRepository<FailedEventLog, Long> {

}