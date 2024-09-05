package com.dife.api.controller;

import com.dife.api.model.dto.BookmarkCreateRequestDto;
import com.dife.api.model.dto.BookmarkResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Bookmark API", description = "북마크 서비스 API")
public interface SwaggerBookmarkController {

	@Operation(summary = "모든 북마크 조회 API", description = "사용자가 생성한 모든 (채팅/게시글) 북마크를 조회해주는 API입니다.")
	@ApiResponse(
			responseCode = "200",
			description = "단일 북마크 조회 예시",
			content = {
				@Content(
						mediaType = "application/json",
						schema = @Schema(implementation = BookmarkResponseDto.class))
			})
	ResponseEntity<List<BookmarkResponseDto>> getAllBookmarks(Authentication authentication);

	@Operation(
			summary = "북마크 생성 API",
			description = "사용자가 DTO를 작성해 채팅/게시글 북마크를 생성하는 API입니다. 북마크 타입 : CHAT, POST, COMMENT")
	@ApiResponse(
			responseCode = "201",
			description = "북마크 생성 성공 예시",
			content = {
				@Content(
						mediaType = "application/json",
						schema = @Schema(implementation = BookmarkResponseDto.class))
			})
	ResponseEntity<BookmarkResponseDto> createBookmark(
			BookmarkCreateRequestDto requestDto, Authentication auth);

	@Operation(
			summary = "채팅방에서의 북마크 조회 API",
			description = "조회하고자 하는 채팅방의 Id를 입력해 해당 채팅방에서 작성한 북마크를 확인할 수 있도록 합니다.")
	@ApiResponse(
			responseCode = "200",
			description = "단일 북마크 조회 예시",
			content = {
				@Content(
						mediaType = "application/json",
						schema = @Schema(implementation = BookmarkResponseDto.class))
			})
	ResponseEntity<List<BookmarkResponseDto>> getBookmarkChats(
			@PathVariable(name = "chatroomId") Long chatroomId, Authentication authentication);

	@Operation(summary = "북마크 취소 API", description = "사용자가 DTO를 작성해 게시글/댓글 북마크를 취소하는 API입니다.")
	@ApiResponse(responseCode = "200")
	ResponseEntity<Void> deleteBookmarkPost(BookmarkCreateRequestDto requestDto, Authentication auth);
}
