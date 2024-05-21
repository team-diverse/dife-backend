package com.dife.api.controller;

import com.dife.api.model.dto.ChatDto;
import com.dife.api.service.ChatroomService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/chat")
@Slf4j
public class ChatController {

	private final ChatroomService chatroomService;

	@GetMapping
	public ResponseEntity<List<ChatDto>> getChats(@RequestParam(name = "room_id") Long room_id) {
		List<ChatDto> chats = chatroomService.getChats(room_id);
		return ResponseEntity.ok(chats);
	}
}
