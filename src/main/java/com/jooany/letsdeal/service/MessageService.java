package com.jooany.letsdeal.service;

import com.jooany.letsdeal.controller.dto.response.MessageGroupRes;
import com.jooany.letsdeal.controller.dto.response.MessageListRes;
import com.jooany.letsdeal.controller.dto.response.MessageRes;
import com.jooany.letsdeal.repository.MessageGroupRepository;
import com.jooany.letsdeal.repository.mapper.MessageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageGroupRepository messageGroupRepository;
    private final MessageMapper messageMapper;

    public Page<MessageGroupRes> getMessageGroupList(Pageable pageable, Long userId){
        Map<String, Object> req = new HashMap<>();
        req.put("pageSize", pageable.getPageSize());
        req.put("offset", pageable.getOffset());
        req.put("userId", userId);
        List<MessageGroupRes> messageGroupResList = messageMapper.findAllMessageGroupByUserId(req);
        int total = messageMapper.getCountMessageGroupByUserId(userId);
        return new PageImpl<>(messageGroupResList, pageable, total);
    }

    public MessageListRes getMessageList(Long messageGroupId, Long saleId, String title, String thumbnailImageUrl, Boolean wasSaleDeleted, Long opponentId, String opponentName, Long userId){
        boolean wasDeletedByOpponent = false;
        Map<String, Object> req = new HashMap<>();
        req.put("messageGroupId", messageGroupId);
        req.put("userId", userId);
        req.put("opponentId", opponentId);

        List<MessageRes> messageList = messageMapper.findAllMessageByMessageGroupId(req);
        int deleteCnt = messageMapper.getCountDeletedByOpponent(req);
        if(deleteCnt > 0){
            wasDeletedByOpponent = true;
        }
        return new MessageListRes(saleId, title, thumbnailImageUrl, wasSaleDeleted, opponentId, opponentName, wasDeletedByOpponent, messageList);
    }
}
