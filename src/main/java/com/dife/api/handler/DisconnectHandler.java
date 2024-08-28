package com.dife.api.handler;

import com.dife.api.model.Chatroom;
import com.dife.api.model.ChatroomSetting;
import com.dife.api.model.ChatroomType;
import com.dife.api.model.Member;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

@Configuration
@RequiredArgsConstructor
public class DisconnectHandler {

	private final SimpMessageSendingOperations messagingTemplate;

	public boolean canEnterChatroom(
			Chatroom chatroom, Member member, String sessionId, String password) {

		if (!isValidGroupChatroom(chatroom, password)
				|| isFull(chatroom)
				|| isExistsAlready(chatroom, member)) {
			return false;
		}
		return true;
	}

	public boolean isExitDisconnectChecked(Chatroom chatroom, String sessionId) {

		if (isEmpty(chatroom)) {
			disconnect(chatroom.getId(), sessionId);
			return false;
		}
		return true;
	}

	private boolean isValidGroupChatroom(Chatroom chatroom, String password) {
		return isGroupChatroom(chatroom) && !isRestrictedGroupChatroom(chatroom, password);
	}

	private boolean isGroupChatroom(Chatroom chatroom) {
		return chatroom.getChatroomType() == ChatroomType.GROUP;
	}

	private boolean isExistsAlready(Chatroom chatroom, Member member) {
		return chatroom.getMembers().contains(member);
	}

	private boolean isRestrictedGroupChatroom(Chatroom chatroom, String password) {
		return !chatroom.getChatroomSetting().getIsPublic() && isWrongPassword(chatroom, password);
	}

	public boolean isWrongPassword(Chatroom chatroom, String given_password) {
		ChatroomSetting setting = chatroom.getChatroomSetting();
		String password = setting.getPassword();

		return !password.equals(given_password);
	}

	public boolean isFull(Chatroom chatroom) {
		ChatroomSetting setting = chatroom.getChatroomSetting();
		return setting.getCount() >= setting.getMaxCount();
	}

	public boolean isEmpty(Chatroom chatroom) {
		ChatroomSetting setting = chatroom.getChatroomSetting();
		return setting.getCount() < 1;
	}

	public boolean isChatroomMember(Chatroom chatroom, Member member) {
		Set<Member> chatroomMembers = chatroom.getMembers();
		return chatroomMembers.contains(member);
	}

	public void disconnect(Long chatroomId, String sessionId) {
		StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.DISCONNECT);
		accessor.setSessionId(sessionId);
		accessor.setDestination("/sub/chatroom/" + chatroomId);
		messagingTemplate.convertAndSend(
				"/sub/chatroom/" + chatroomId, "Disconnect", accessor.getMessageHeaders());
	}
}
