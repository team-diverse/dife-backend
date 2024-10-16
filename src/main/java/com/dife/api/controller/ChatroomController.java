package com.dife.api.controller;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.ok;

import com.dife.api.model.ChatroomType;
import com.dife.api.model.GroupPurposeType;
import com.dife.api.model.dto.*;
import com.dife.api.service.ChatroomService;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
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
	public ResponseEntity<Void> checkChatroomName(@RequestParam(name = "name") String name) {
		Boolean isDuplicate = chatroomService.isDuplicate(name);

		if (isDuplicate) {
			return ResponseEntity.status(CONFLICT).build();
		}
		return ResponseEntity.ok().build();
	}

	@GetMapping
	public ResponseEntity<List<ChatroomResponseDto>> getChatrooms(
			@RequestParam(name = "type", required = false) ChatroomType type,
			Authentication authentication) {
		List<ChatroomResponseDto> responseDto =
				chatroomService.getChatrooms(type, authentication.getName());
		return ResponseEntity.status(OK).body(responseDto);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ChatroomResponseDto> getChatroom(
			@PathVariable(name = "id") Long chatroomId, Authentication auth) {
		ChatroomResponseDto responseDto = chatroomService.getChatroomById(chatroomId, auth.getName());
		return ResponseEntity.status(OK).body(responseDto);
	}

	@PostMapping(consumes = "multipart/form-data")
	public ResponseEntity<ChatroomResponseDto> createChatroom(
			@RequestParam(name = "profileImg", required = false) MultipartFile profileImg,
			@RequestParam(name = "chatroomType") ChatroomType chatroomType,
			@RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "description", required = false) String description,
			@RequestParam(name = "toMemberId", required = false) Long toMemberId,
			@RequestParam(name = "hobbies", required = false) Set<String> hobbies,
			@RequestParam(name = "maxCount", required = false) Optional<Integer> maxCount,
			@RequestParam(name = "purposes", required = false) Set<GroupPurposeType> purposes,
			@RequestParam(name = "languages", required = false) Set<String> languages,
			@RequestParam(name = "isPublic", required = false) Boolean isPublic,
			@RequestParam(name = "password", required = false) String password,
			Authentication authentication)
			throws IOException {

		ChatroomResponseDto responseDto =
				chatroomService.createChatroom(
						profileImg,
						chatroomType,
						name,
						description,
						toMemberId,
						maxCount,
						purposes,
						hobbies,
						languages,
						isPublic,
						password,
						authentication.getName());

		return ResponseEntity.status(CREATED).body(responseDto);
	}

	@PutMapping("/{id}")
	public ResponseEntity<GroupChatroomResponseDto> update(
			@PathVariable(name = "id", required = false) Long id,
			@RequestParam(name = "profileImg", required = false) MultipartFile profileImg,
			@RequestParam(name = "hobbies", required = false) Set<String> hobbies,
			@RequestParam(name = "maxCount", required = false) Optional<Integer> maxCount,
			@RequestParam(name = "purpose", required = false) Set<GroupPurposeType> purposes,
			@RequestParam(name = "languages", required = false) Set<String> languages,
			@RequestParam(name = "isPublic", required = false) Boolean isPublic,
			@RequestParam(name = "password", required = false) String password,
			Authentication auth)
			throws IOException {

		GroupChatroomResponseDto responseDto =
				chatroomService.update(
						id,
						profileImg,
						maxCount,
						purposes,
						hobbies,
						languages,
						isPublic,
						password,
						auth.getName());
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
	public ResponseEntity<List<GroupChatroomResponseDto>> getFilterChatrooms(
			@RequestParam(name = "hobbies", required = false) Set<String> hobbies,
			@RequestParam(name = "languages", required = false) Set<String> languages,
			@RequestParam(name = "purposes", required = false) Set<GroupPurposeType> purposes,
			@RequestParam(name = "maxCount", required = false, defaultValue = "30") Integer maxCount,
			Authentication auth) {
		List<GroupChatroomResponseDto> responseDto =
				chatroomService.getFilterChatrooms(hobbies, purposes, languages, maxCount, auth.getName());
		return ok(responseDto);
	}

	@GetMapping("/search")
	public ResponseEntity<List<GroupChatroomResponseDto>> getSearchChatrooms(
			@RequestParam(name = "keyword") String keyword, Authentication auth) {
		List<GroupChatroomResponseDto> responseDto =
				chatroomService.getSearchChatrooms(keyword, auth.getName());
		return ok(responseDto);
	}

	@GetMapping("/likes")
	public ResponseEntity<List<GroupChatroomResponseDto>> getLikeChatrooms(Authentication auth) {
		List<GroupChatroomResponseDto> responseDto = chatroomService.getLikedChatrooms(auth.getName());
		return ResponseEntity.ok(responseDto);
	}

	@GetMapping("/random")
	public ResponseEntity<List<GroupChatroomResponseDto>> getRandomChatrooms(
			@RequestParam(name = "count", defaultValue = "1") int count, Authentication auth) {
		List<GroupChatroomResponseDto> responseDto =
				chatroomService.getRandomChatrooms(count, auth.getName());
		return ResponseEntity.ok(responseDto);
	}
}
