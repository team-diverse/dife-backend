package com.dife.api.controller;

import com.dife.api.model.ChatRoom;
import com.dife.api.model.dto.GroupChatRoomDto;
import com.dife.api.service.ChatRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/chat")
@Slf4j
@Tag(name="Chat API", description = "Chat API")
public class ChatController {

    private final ChatRoomService chatRoomService;

    @Operation(summary = "그룹 채팅방 생성", description = "사용자가 그룹 채팅방 생성")
    @PostMapping
    public ResponseEntity<GroupChatRoomDto> createGroupChatRoom(@RequestParam (value = "chatRoomName") String chatRoomName,
                                                                @RequestParam (value = "chatRoomBio") String chatRoomBio,
                                                                @RequestParam (value = "max_count") String max_count,
                                                                @RequestParam (value = "min_count") String min_count) {

        ChatRoom chatRoom = chatRoomService.createGroupChatRoom(chatRoomName, chatRoomBio, max_count, min_count);

        return ResponseEntity.status(HttpStatus.CREATED).body(new GroupChatRoomDto(chatRoom));
    }


}
