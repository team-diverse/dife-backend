package com.dife.api.model.dto;

import com.dife.api.model.Chatroom;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupChatroomlistDto {
	private Long id;
	private String room_name;
	private Boolean is_public;
	private String username;

	public GroupChatroomlistDto(Chatroom chatroom) {
		this.id = chatroom.getId();
		this.room_name = chatroom.getName();
		this.is_public = chatroom.getChatroom_setting().getIs_public();
		if (chatroom.getMember().getIs_public()) this.username = chatroom.getMember().getUsername();
	}
}
