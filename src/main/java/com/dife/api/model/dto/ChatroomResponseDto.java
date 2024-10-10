package com.dife.api.model.dto;

import com.dife.api.model.ChatroomType;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.*;

public interface ChatroomResponseDto {

	Long getId();

	LocalDateTime getCreated();

	ChatroomType getChatroomType();

	Set<MemberRestrictedResponseDto> getMembers();

	Set<MemberRestrictedResponseDto> getExitedMembers();
}
