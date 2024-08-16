package com.dife.api.controller;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.ok;

import com.dife.api.model.ChatroomType;
import com.dife.api.model.dto.*;
import com.dife.api.service.ChatroomService;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/chatrooms")
@Slf4j
public class ChatroomController implements SwaggerChatroomController {

	private final ChatroomService chatroomService;

	@RequestMapping(value = "/check", method = RequestMethod.HEAD)
	public ResponseEntity<Void> checkUsername(@RequestParam(name = "name") String name) {
		Boolean isDuplicate = chatroomService.isDuplicate(name);

		if (isDuplicate) {
			return ResponseEntity.status(CONFLICT).build();
		}
		return ResponseEntity.ok().build();
	}

	@GetMapping
	public ResponseEntity<List<ChatroomResponseDto>> getGroupChatrooms(
			ChatroomType chatroomType, Authentication authentication) {
		List<ChatroomResponseDto> responseDto =
				chatroomService.getChatrooms(chatroomType, authentication.getName());
		return ResponseEntity.status(OK).body(responseDto);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ChatroomResponseDto> getGroupChatroom(
			@PathVariable(name = "id") Long chatroomId, Authentication auth) {
		ChatroomResponseDto responseDto = chatroomService.getChatroom(chatroomId, auth.getName());
		return ResponseEntity.status(OK).body(responseDto);
	}

	@PostMapping(consumes = "multipart/form-data")
	public ResponseEntity<ChatroomResponseDto> createChatroom(
			@RequestParam(name = "profileImg", required = false) MultipartFile profileImg,
			@RequestParam(name = "chatroomType") ChatroomType chatroomType,
			@RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "description", required = false) String description,
			@RequestParam(name = "toMemberId", required = false) Long toMemberId,
			Authentication authentication) {

		ChatroomResponseDto responseDto =
				chatroomService.createChatroom(
						profileImg, chatroomType, name, description, toMemberId, authentication.getName());

		return ResponseEntity.status(CREATED).body(responseDto);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ChatroomResponseDto> update(
			@RequestBody GroupChatroomPutRequestDto requestDto,
			@PathVariable(name = "id") Long chatroomId,
			Authentication auth) {

		ChatroomResponseDto responseDto =
				chatroomService.update(requestDto, chatroomId, auth.getName());
		return ResponseEntity.status(OK).body(responseDto);
	}

	@GetMapping("/{roomId}/{memberId}")
	public ResponseEntity<Void> kickoutMember(
			@PathVariable(name = "roomId") Long roomId,
			@PathVariable(name = "memberId") Long memberId,
			Authentication auth) {
		chatroomService.kickout(roomId, memberId, auth.getName());

		return new ResponseEntity<>(OK);
	}

	@GetMapping("/filter")
	public ResponseEntity<List<ChatroomResponseDto>> getFilterChatrooms(
			@RequestParam(name = "hobbies", required = false) Set<String> hobbies,
			@RequestParam(name = "languages", required = false) Set<String> languages,
			@RequestParam(name = "purposes", required = false) Set<String> purposes,
			@RequestParam(name = "minCount", required = false, defaultValue = "3") Integer minCount,
			@RequestParam(name = "maxCount", required = false, defaultValue = "30") Integer maxCount) {
		List<ChatroomResponseDto> responseDto =
				chatroomService.getFilterChatrooms(hobbies, languages, purposes, minCount, maxCount);
		return ok(responseDto);
	}

	@GetMapping("/search")
	public ResponseEntity<List<ChatroomResponseDto>> getFilterChatrooms(
			@RequestParam(name = "keyword") String keyword) {
		List<ChatroomResponseDto> responseDto = chatroomService.getSearchChatrooms(keyword);
		return ok(responseDto);
	}

	@GetMapping("/likes")
	public ResponseEntity<List<ChatroomResponseDto>> getLikeChatrooms(Authentication auth) {
		List<ChatroomResponseDto> responseDto = chatroomService.getLikedChatrooms(auth.getName());
		return ResponseEntity.ok(responseDto);
	}
}
