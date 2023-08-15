package com.jooany.letsdeal.service;

import com.jooany.letsdeal.controller.dto.request.MessageSendReq;
import com.jooany.letsdeal.controller.dto.response.MessageGroupRes;
import com.jooany.letsdeal.controller.dto.response.MessageListRes;
import com.jooany.letsdeal.controller.dto.response.MessageRes;
import com.jooany.letsdeal.controller.dto.response.MessageSendRes;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageGroupRepository messageGroupRepository;
    private final MessageMapper messageMapper;

    public Page<MessageGroupRes> getMessageGroupList(Pageable pageable, Long userId) {
        Map<String, Object> req = new HashMap<>();
        req.put("pageSize", pageable.getPageSize());
        req.put("offset", pageable.getOffset());
        req.put("userId", userId);
        List<MessageGroupRes> messageGroupResList = messageMapper.findAllMessageGroupByUserId(req);
        int total = messageMapper.getCountMessageGroupByUserId(userId);
        return new PageImpl<>(messageGroupResList, pageable, total);
    }

    public MessageListRes getMessageList(Long messageGroupId, Long saleId, String title, String thumbnailImageUrl, Boolean wasSaleDeleted, Long opponentId, String opponentName, Long userId) {

        Map<String, Object> req = new HashMap<>();
        req.put("messageGroupId", messageGroupId);
        req.put("userId", userId);
        req.put("opponentId", opponentId);

        // 사용자 접근 권한 체크
        if (!messageMapper.checkPermissionToRead(req)) {
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

    @Transactional
    public MessageSendRes sendMessage(MessageSendReq req) {

        // 상대방이 탈퇴한 회원이라면 에러 발생
        if(messageMapper.checkOpponentWithdrawn(req.getOpponentId())){
            throw new LetsDealAppException(ErrorCode.USER_NOT_FOUND);
        }

        if (req.getMessageGroupId() == null) { // 요청 그룹 아이디가 없는 경우, 복구 가능한 그룹 찾기 or 그룹 생성

            // 삭제 복구 가능한 그룹 아이디 조회
            Long deletedGroupId = findGroupId(req);

            if (deletedGroupId == null) { // 삭제 복구 가능한 그룹이 존재하지 않는 경우, 새 그룹 생성
                Long newGroupId = saveNewMessageGroup(req);
                req.setMessageGroupId(newGroupId);

            } else { // 삭제 복구 가능한 그룹이 존재하는 경우, 그룹 삭제 원복
                messageMapper.updateGroupDeleteToNull(deletedGroupId);
                req.setMessageGroupId(deletedGroupId);
            }
        }

        // 메시지 전송 mapper
        messageMapper.saveMessage(req);

        return new MessageSendRes(req.getMessageGroupId());
    }

    private Long findGroupId(MessageSendReq req) {
        Long messageGroupId;

        if (req.getSellerId() == req.getUserId()) { // 내가 판매자인 경우
            messageGroupId = messageMapper.findGroupIdWhenIsSeller(req);
        } else { // 내가 구매자인 경우
            messageGroupId = messageMapper.findGroupIdWhenIsBuyer(req);
        }

        return messageGroupId;
    }

    private Long saveNewMessageGroup(MessageSendReq req){
        if (req.getSellerId() == req.getUserId()) { // 내가 판매자인 경우
            messageMapper.saveMessageGroupWhenIsSeller(req);
            return messageMapper.findGroupIdWhenIsSeller(req);
        }else{ // 내가 구매자인 경우
            messageMapper.saveMessageGroupWhenIsBuyer(req);
            return messageMapper.findGroupIdWhenIsBuyer(req);
        }
    }
}
