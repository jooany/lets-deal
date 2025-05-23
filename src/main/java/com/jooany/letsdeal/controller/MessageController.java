package com.jooany.letsdeal.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jooany.letsdeal.controller.dto.UserDto;
import com.jooany.letsdeal.controller.dto.request.MessageAllDeleteReq;
import com.jooany.letsdeal.controller.dto.request.MessageListReq;
import com.jooany.letsdeal.controller.dto.request.MessageSendReq;
import com.jooany.letsdeal.controller.dto.response.MessageGroupRes;
import com.jooany.letsdeal.controller.dto.response.MessageListRes;
import com.jooany.letsdeal.controller.dto.response.MessageSendRes;
import com.jooany.letsdeal.controller.dto.response.Response;
import com.jooany.letsdeal.service.MessageService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class MessageController {

	private final MessageService messageService;

	@GetMapping
	public Response<Page<MessageGroupRes>> getMessageGroupList(Pageable pageable, Authentication authentication) {
		UserDto userDto = (UserDto)authentication.getPrincipal();
		return Response.success(messageService.getMessageGroupList(pageable, userDto.getId()));
	}

	@PostMapping("/{messageGroupId}")
	public Response<MessageListRes> getMessageList(@PathVariable Long messageGroupId,
		@RequestBody @Valid MessageListReq req,
		Authentication authentication) {
		UserDto userDto = (UserDto)authentication.getPrincipal();
		return Response.success(messageService.getMessageList(messageGroupId, req.getOpponentId(), userDto.getId()));
	}

	@PostMapping
	public Response<MessageSendRes> sendMessage(@RequestBody @Valid MessageSendReq req, Authentication authentication) {
		UserDto userDto = (UserDto)authentication.getPrincipal();
		req.setUserId(userDto.getId());
		return Response.success(messageService.sendMessage(req));
	}

	@PostMapping("/delete")
	public Response<Void> deleteMessages(@RequestBody @Valid MessageAllDeleteReq req, Authentication authentication) {
		UserDto userDto = (UserDto)authentication.getPrincipal();
		req.setUserId(userDto.getId());
		messageService.deleteMessages(req);
		return Response.success();
	}
}
