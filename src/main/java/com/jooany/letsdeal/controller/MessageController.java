package com.jooany.letsdeal.controller;

import com.jooany.letsdeal.controller.dto.UserDto;
import com.jooany.letsdeal.controller.dto.request.MessageListReq;
import com.jooany.letsdeal.controller.dto.request.MessageSendReq;
import com.jooany.letsdeal.controller.dto.response.MessageGroupRes;
import com.jooany.letsdeal.controller.dto.response.MessageListRes;
import com.jooany.letsdeal.controller.dto.response.MessageSendRes;
import com.jooany.letsdeal.controller.dto.response.Response;
import com.jooany.letsdeal.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final MessageService messageService;

    @GetMapping
    public Response<Page<MessageGroupRes>> getMessageGroupList(Pageable pageable, Authentication authentication) {
        UserDto userDto = (UserDto) authentication.getPrincipal();
        return Response.success(messageService.getMessageGroupList(pageable, userDto.getId()));
    }

    @PostMapping("/{messageGroupId}")
    public Response<MessageListRes> getMessageList(@PathVariable Long messageGroupId, @RequestBody MessageListReq req, Authentication authentication) {
        UserDto userDto = (UserDto) authentication.getPrincipal();
        return Response.success(messageService.getMessageList(messageGroupId, req.getSaleId(), req.getTitle(), req.getThumbnailImageUrl(), req.getWasSaleDeleted(), req.getOpponentId(), req.getOpponentName(), userDto.getId()));
    }

    @PostMapping
    public Response<MessageSendRes> sendMessage(@RequestBody MessageSendReq req, Authentication authentication){
        UserDto userDto = (UserDto) authentication.getPrincipal();
        req.setUserId(userDto.getId());
        return Response.success(messageService.sendMessage(req));
    }
}
