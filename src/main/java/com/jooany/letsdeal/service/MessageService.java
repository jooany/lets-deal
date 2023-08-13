package com.jooany.letsdeal.service;

import com.jooany.letsdeal.controller.dto.response.MessageGroupRes;
import com.jooany.letsdeal.controller.dto.response.MessageListRes;
import com.jooany.letsdeal.controller.dto.response.MessageRes;
import com.jooany.letsdeal.exception.ErrorCode;
import com.jooany.letsdeal.exception.LetsDealAppException;
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

        Map<String, Object> req = new HashMap<>();
        req.put("messageGroupId", messageGroupId);
        req.put("userId", userId);
        req.put("opponentId", opponentId);

        // 사용자 접근 권한 체크
        if(!messageMapper.checkPermissionToRead(req)){
            throw new LetsDealAppException(ErrorCode.INVALID_PERMISSION);
        }

        // 수신자 읽음 처리
        messageMapper.updateUnreadToRead(req);

        // 상대방이 탈퇴한 유저인지 확인하기 위한 값 조회
        boolean isOpponentWithdrawn = messageMapper.checkOpponentWithdrawn(opponentId);

        // 1:1 메시지 데이터 조회
        List<MessageRes> messageList = messageMapper.findAllMessageByMessageGroupId(req);
        return new MessageListRes(saleId, title, thumbnailImageUrl, wasSaleDeleted, opponentId, opponentName, isOpponentWithdrawn, messageList);
    }
}
