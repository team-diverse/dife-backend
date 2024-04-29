package com.dife.api.controller;

import com.dife.api.model.Chatroom;
import com.dife.api.model.dto.GroupChatroomDto;
import com.dife.api.service.ChatroomService;
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
@RequestMapping("/api/chats")
@Slf4j
@Tag(name = "Chat API", description = "Chat API")
public class ChatController {

    private final ChatroomService chatroomService;

    @Operation(summary = "그룹 채팅방 생성", description = "사용자가 그룹 채팅방 생성")
    @PostMapping
    public ResponseEntity<GroupChatroomDto> createGroupChatroom(@RequestParam (value = "name") String name,
                                                                @RequestParam (value = "description") String description,
                                                                @RequestParam (value = "max_count") String max_count,
                                                                @RequestParam (value = "min_count") String min_count) {

        Chatroom chatroom = chatroomService.createGroupChatroom(name, description, max_count, min_count);

        return ResponseEntity.status(HttpStatus.CREATED).body(new GroupChatroomDto(chatroom));
    }


}
