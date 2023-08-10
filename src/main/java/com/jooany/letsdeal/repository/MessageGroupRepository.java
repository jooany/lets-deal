package com.jooany.letsdeal.repository;

import com.jooany.letsdeal.model.entity.MessageGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageGroupRepository extends JpaRepository<MessageGroup, Long>, MessageGroupCustomRepository{
}