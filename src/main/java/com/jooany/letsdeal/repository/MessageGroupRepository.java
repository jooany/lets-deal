package com.jooany.letsdeal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jooany.letsdeal.model.entity.MessageGroup;

@Repository
public interface MessageGroupRepository extends JpaRepository<MessageGroup, Long> {
}