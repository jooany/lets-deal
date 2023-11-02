package com.jooany.letsdeal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jooany.letsdeal.model.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
}