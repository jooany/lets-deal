package com.jooany.letsdeal.repository.mapper;

import com.jooany.letsdeal.controller.dto.response.MessageGroupRes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MessageMapper {
    int getCountMessageGroupByUserId (Long id);
    List<MessageGroupRes> findAllMessageGroupByUserId(Map<String, Object> req);

}
