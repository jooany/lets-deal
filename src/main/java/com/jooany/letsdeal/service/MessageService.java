package com.jooany.letsdeal.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jooany.letsdeal.controller.dto.request.MessageAllDeleteReq;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {
	private final MessageGroupRepository messageGroupRepository;
	private final MessageMapper messageMapper;

	@Transactional(readOnly = true)
	public Page<MessageGroupRes> getMessageGroupList(Pageable pageable, Long userId) {
		Map<String, Object> req = new HashMap<>();
		req.put("pageSize", pageable.getPageSize());
		req.put("offset", pageable.getOffset());
		req.put("userId", userId);
		List<MessageGroupRes> messageGroupResList = messageMapper.findAllMessageGroupByUserId(req);
		int total = messageMapper.getCountMessageGroupByUserId(userId);
		return new PageImpl<>(messageGroupResList, pageable, total);
	}

	@Transactional
	public MessageListRes getMessageList(Long messageGroupId, Long opponentId, Long userId) {

		Map<String, Object> req = new HashMap<>();
		req.put("messageGroupId", messageGroupId);
		req.put("userId", userId);
		req.put("opponentId", opponentId);

		if (!messageMapper.checkPermissionToRead(req)) {
			throw new LetsDealAppException(ErrorCode.INVALID_PERMISSION);
		}

		messageMapper.updateUnreadToRead(req);

		boolean isOpponentWithdrawn = messageMapper.checkOpponentWithdrawn(opponentId);

		List<MessageRes> messageList = messageMapper.findAllMessageByMessageGroupId(req);
		return new MessageListRes(isOpponentWithdrawn, messageList);
	}

	@Transactional
	public MessageSendRes sendMessage(MessageSendReq req) {
		if (messageMapper.checkOpponentWithdrawn(req.getOpponentId())) {
			throw new LetsDealAppException(ErrorCode.USER_NOT_FOUND);
		}

		if (req.getMessageGroupId() == null) {
			Long deletedGroupId = findGroupId(req);

			if (deletedGroupId != null) {
				messageMapper.updateGroupDeleteToNull(deletedGroupId);
				req.setMessageGroupId(deletedGroupId);

			} else {
				Long newGroupId = saveNewMessageGroup(req);
				req.setMessageGroupId(newGroupId);
			}
		}

		messageMapper.saveMessage(req);

		return new MessageSendRes(req.getMessageGroupId());
	}

	/**
	 * @param req 특이사항 : 메시지그룹 & 메시지는 1명만 삭제하면 Soft Delete(=update), 2명 모두 삭제하면 Hard Delete 한다.
	 */

	@Transactional
	public void deleteMessages(MessageAllDeleteReq req) {
		Long groupId = req.getMessageGroupId();

		messageMapper.deleteMessagesByIds(req);

		if (!messageMapper.checkHasUndeletedMessage(groupId)) {
			messageMapper.deleteMessageGroupById(groupId);

		} else {
			if (!messageMapper.checkHasUnreadMessage(req)) {
				messageMapper.updateMessageGroupToDeleteById(req);
			}
			messageMapper.updateMessagesToDeleteByIds(req);
		}
	}

	@Transactional
	public void deleteWithdrawnUserMessages(Long userId) {
		messageMapper.updateMessagesToDeleteByUserId(userId);
		messageMapper.updateMessageGroupToDeleteByUserId(userId);
		messageMapper.deleteMessagesByUserId(userId);
		messageMapper.deleteMessageGroupByUserId(userId);
	}

	private Long findGroupId(MessageSendReq req) {
		Long messageGroupId;

		if (req.getSellerId() == req.getUserId()) {
			messageGroupId = messageMapper.findGroupIdWhenIsSeller(req);
		} else {
			messageGroupId = messageMapper.findGroupIdWhenIsBuyer(req);
		}

		return messageGroupId;
	}

	private Long saveNewMessageGroup(MessageSendReq req) {
		if (req.getSellerId() == req.getUserId()) {
			messageMapper.saveMessageGroupWhenIsSeller(req);
			return messageMapper.findGroupIdWhenIsSeller(req);
		} else {
			messageMapper.saveMessageGroupWhenIsBuyer(req);
			return messageMapper.findGroupIdWhenIsBuyer(req);
		}
	}
}
