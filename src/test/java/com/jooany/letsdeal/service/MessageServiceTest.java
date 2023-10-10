package com.jooany.letsdeal.service;

import com.jooany.letsdeal.controller.dto.response.MessageGroupRes;
import com.jooany.letsdeal.controller.dto.response.MessageRes;
import com.jooany.letsdeal.exception.LetsDealAppException;
import com.jooany.letsdeal.repository.mapper.MessageMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class MessageServiceTest {
    @Mock
    private MessageMapper messageMapper;

    @InjectMocks
    private MessageService messageService;

    @DisplayName("메시지그룹 목록 조회 - 성공")
    @Test
    void getMessageGroupList(){
        Long userId = 1L;
        Pageable pageable = mock(Pageable.class);
        int total = 10;
        List<MessageGroupRes> list = new ArrayList<>();
        given(messageMapper.findAllMessageGroupByUserId(anyMap())).willReturn(list);
        given(messageMapper.getCountMessageGroupByUserId(userId)).willReturn(total);

        Assertions.assertDoesNotThrow( () -> messageService.getMessageGroupList(pageable, userId));
    }

    @DisplayName("1:1 메시지 조회 - 성공")
    @Test
    void getMessageList(){
        Long messageGroupId = 1L;
        Long opponentId = 2L;
        Long userId = 1L;
        List<MessageRes> list = new ArrayList<>();
        given(messageMapper.checkPermissionToRead(anyMap())).willReturn(true);
        given(messageMapper.checkOpponentWithdrawn(opponentId)).willReturn(false);
        given(messageMapper.findAllMessageByMessageGroupId(anyMap())).willReturn(list);

        Assertions.assertDoesNotThrow( () -> messageService.getMessageList(messageGroupId, opponentId,  userId));
    }

    @DisplayName("1:1 메시지 조회_사용자가 구매자,판매자,관리자가 아닐 경우 권한 없음 - 실패")
    @Test
    void getMessageList_invalidPermission(){
        Long messageGroupId = 1L;
        Long opponentId = 2L;
        Long userId = 1L;

        given(messageMapper.checkPermissionToRead(anyMap())).willReturn(false);

        Assertions.assertThrows(LetsDealAppException.class, () -> messageService.getMessageList(messageGroupId, opponentId, userId));
    }
}