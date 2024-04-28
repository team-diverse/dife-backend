package com.dife.api.model.dto;

import com.dife.api.model.ChatRoom;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "그룹 채팅방 응답 객체")
public class GroupChatRoomDto {

    @Schema(description = "그룹 채팅방 생성 성공여부", example = "true")
    private Boolean success;

    private Long groupChatRoomId;

    @NotNull(message = "그룹 채팅방 이름을 입력해주세요!")
    @Schema(description = "그룹 채팅방 이름", example = "21학번 모여라")
    private String chatRoomName;

    @NotNull(message = "그룹 채팅방의 한줄 소개를 입력해주세요!")
    @Schema(description = "그룹 채팅방 한줄 소개", example = "21학번 정보 공유 및 잡담 채팅방")
    private String chatRoomBio;

    @Schema(description = "채팅방 생성 일시")
    private LocalDateTime created;

    public GroupChatRoomDto(ChatRoom chatRoom)
    {
        this.success = true;
        this.groupChatRoomId = chatRoom.getId();
        this.chatRoomName = chatRoom.getChatRoomName();
        this.chatRoomBio = chatRoom.getChatRoomSetting().getChatRoomBio();
        this.created = chatRoom.getCreated();
    }
}
