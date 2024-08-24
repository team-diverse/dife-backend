package com.dife.api.controller;

import com.dife.api.model.ChatroomType;
import com.dife.api.model.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Chatroom API", description = "채팅방 관리 서비스 API")
public interface SwaggerChatroomController {

	@Operation(summary = "채팅방 중복 이름 확인 API", description = "사용자가 채팅방 이름을 검색해 중복성을 검사하는 API입니다.")
	@ApiResponse(responseCode = "200")
	ResponseEntity<Void> checkUsername(@RequestParam(name = "name") String name);

	@Operation(
			summary = "채팅방 생성1 API",
			description = "사용자가 multipart/form-data 형태의 POST요청으로 그룹 채팅방을 생성하는 API입니다.")
	@ApiResponse(
			responseCode = "201",
			description = "그룹 채팅방1 생성 성공 예시",
			content = {
				@Content(
						mediaType = "application/json",
						schema = @Schema(implementation = ChatroomResponseDto.class))
			})
	ResponseEntity<ChatroomResponseDto> createChatroom(
			@RequestParam(name = "profileImg", required = false) MultipartFile profileImg,
			@RequestParam(name = "chatroomType") ChatroomType chatroomType,
			@RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "description", required = false) String description,
			@RequestParam(name = "toMemberId", required = false) Long toMemberId,
			@RequestParam(name = "hobbies", required = false) Set<String> hobbies,
			@RequestParam(name = "maxCount", required = false) Optional<Integer> maxCount,
			@RequestParam(name = "purposes", required = false) Set<String> purposes,
			@RequestParam(name = "languages", required = false) Set<String> languages,
			@RequestParam(name = "isPublic", required = false) Boolean isPublic,
			@RequestParam(name = "password", required = false) String password,
			Authentication authentication);

	@Operation(
			summary = "채팅방 세부사항 업데이트 API",
			description = "사용자가 DTO를 작성해 PUT요청으로 그룹 채팅방 정보 업데이트를 진행하는 API입니다. 방장만이 접근할 수 있는 API입니다.")
	@ApiResponse(
			responseCode = "200",
			description = "그룹 채팅방 업데이트 성공 예시",
			content = {
				@Content(
						mediaType = "application/json",
						schema = @Schema(implementation = ChatroomResponseDto.class))
			})
	ResponseEntity<ChatroomResponseDto> update(
			@PathVariable(name = "id", required = false) Long id,
			@RequestParam(name = "profileImg", required = false) MultipartFile profileImg,
			@RequestParam(name = "hobbies", required = false) Set<String> hobbies,
			@RequestParam(name = "maxCount", required = false) Optional<Integer> maxCount,
			@RequestParam(name = "purpose", required = false) Set<String> purposes,
			@RequestParam(name = "languages", required = false) Set<String> languages,
			@RequestParam(name = "isPublic", required = false) Boolean isPublic,
			@RequestParam(name = "password", required = false) String password,
			Authentication auth);

	@Operation(summary = "채팅방 회원 강퇴 API", description = "채팅방 방장만이 회원 강퇴 권한을 갖는 API입니다.")
	@ApiResponse(responseCode = "200", description = "그룹 채팅방 강퇴 성공 예시")
	ResponseEntity<Void> kickoutMember(
			@PathVariable(name = "roomId") Long roomId,
			@PathVariable(name = "memberId") Long memberId,
			Authentication auth);

	@Operation(
			summary = "채팅방 전체 조회 API",
			description =
					"조회하고자 하는 채팅방 타입(그룹/싱글)을 입력해 속한 채팅방들을 조회하는 API입니다. 그룹의 경우 그냥 GROUP만 입력값으로 넣고 싱글의 경우 SINGLE과 더불어 조회하고자 하는 상대방의 id를 입력값으로 넣어야 합니다.")
	@ApiResponse(
			responseCode = "200",
			description = "채팅방 조회 성공 예시",
			content = {
				@Content(
						mediaType = "application/json",
						schema = @Schema(implementation = ChatroomResponseDto.class))
			})
	ResponseEntity<List<ChatroomResponseDto>> getGroupChatrooms(
			ChatroomType chatroomType, Authentication authentication);

	@Operation(summary = "채팅방 조회 API", description = "그룹, 싱글 모든 채팅방을 Id로 조회해주는 API입니다.")
	@ApiResponse(
			responseCode = "200",
			description = "채팅방 조회 성공 예시",
			content = {
				@Content(
						mediaType = "application/json",
						schema = @Schema(implementation = ChatroomResponseDto.class))
			})
	ResponseEntity<ChatroomResponseDto> getGroupChatroom(
			@PathVariable(name = "id") Long id, Authentication auth) throws IOException;

	@Operation(
			summary = "그룹 채팅방 필터 검색 조회 API",
			description =
					"세부적인 그룹 채팅방 조회 필터링 선택지를 사용자에게 제시해 해당하는 그룹 채팅방을 조회할 수 있게 됩니다. 채팅방 목적, 취미, 언어의 복수 선택, 단일 종류 선택 가능한 name Set을 입력받게 됩니다.")
	@ApiResponse(
			responseCode = "200",
			description = "채팅방 조회 성공 예시",
			content = {
				@Content(
						mediaType = "application/json",
						schema = @Schema(implementation = ChatroomResponseDto.class))
			})
	ResponseEntity<List<ChatroomResponseDto>> getFilterChatrooms(
			@RequestParam(name = "hobbies", required = false) Set<String> hobbies,
			@RequestParam(name = "languages", required = false) Set<String> languages,
			@RequestParam(name = "purposes", required = false) Set<String> purposes,
			@RequestParam(name = "maxCount", required = false, defaultValue = "30") Integer maxCount,
			Authentication auth);

	@Operation(
			summary = "채팅방 필터 검색 조회 API",
			description = "채팅방 이름, 한줄 소개에 해당 검색어가 포함되는 채팅방들을 조회하는 API입니다.")
	@ApiResponse(
			responseCode = "200",
			description = "채팅방 조회 성공 예시",
			content = {
				@Content(
						mediaType = "application/json",
						schema = @Schema(implementation = ChatroomResponseDto.class))
			})
	ResponseEntity<List<ChatroomResponseDto>> getFilterChatrooms(
			@RequestParam(name = "keyword") String keyword);

	@Operation(summary = "회원이 좋아요 누른 그룹 채팅방 목록 조회 API")
	@ApiResponse(
			responseCode = "200",
			description = "채팅방 조회 성공 예시",
			content = {
				@Content(
						mediaType = "application/json",
						schema = @Schema(implementation = ChatroomResponseDto.class))
			})
	ResponseEntity<List<ChatroomResponseDto>> getLikeChatrooms(Authentication auth);

	@Operation(
			summary = "홈화면 랜덤 10개 채팅방 조회 API",
			description = "홈화면에 보일 커넥트 준비상태 10개 회원 정보를 확인할 수 있습니다.")
	@ApiResponse(responseCode = "200")
	ResponseEntity<List<ChatroomResponseDto>> getRandomChatrooms(
			@RequestParam(name = "count", defaultValue = "1") int count, Authentication auth);
}
