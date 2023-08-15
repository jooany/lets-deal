package com.jooany.letsdeal.repository.mapper;

import com.jooany.letsdeal.controller.dto.request.MessageAllDeleteReq;
import com.jooany.letsdeal.controller.dto.request.MessageSendReq;
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
    Boolean checkOpponentWithdrawn(Long opponentId);
    void updateUnreadToRead(Map<String, Object> req);
    Boolean checkPermissionToRead(Map<String, Object> req);
    Long findGroupIdWhenIsSeller(MessageSendReq req);
    Long findGroupIdWhenIsBuyer(MessageSendReq req);
    void saveMessageGroupWhenIsSeller(MessageSendReq req);
    void saveMessageGroupWhenIsBuyer(MessageSendReq req);
    void updateGroupDeleteToNull(Long messageGroupId);
    void saveMessage(MessageSendReq req);
    Boolean checkHasUndeletedMessage(Long messageGroupId);
    Boolean checkHasUnreadMessage(MessageAllDeleteReq req);
    void deleteMessagesByIds(MessageAllDeleteReq req);
    void deleteMessageGroupById(Long messageGroupId);
    void updateMessageGroupToDeleteById(MessageAllDeleteReq req);
    void updateMessagesToDeleteByIds(MessageAllDeleteReq req);


}
