package com.jooany.letsdeal.repository.mapper;

import com.jooany.letsdeal.controller.dto.response.MessageGroupRes;
import com.jooany.letsdeal.controller.dto.response.MessageRes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MessageMapper {
    int getCountMessageGroupByUserId (Long userId);
    List<MessageGroupRes> findAllMessageGroupByUserId(Map<String, Object> req);
    List<MessageRes> findAllMessageByMessageGroupId(Map<String, Object> req);
    int getCountDeletedByOpponent(Map<String, Object> req);

}
