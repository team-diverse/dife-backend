package com.dife.api.controller;

import com.dife.api.model.BoardCategory;
import com.dife.api.model.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Board API", description = "게시판 서비스 API")
public interface SwaggerBoardController {

	@Operation(summary = "게시판 조회 API", description = "게시판 종류를 입력해 최신순으로 게시판을 조회합니다.")
	@ApiResponse(responseCode = "200")
	ResponseEntity<List<PostResponseDto>> getPostsByBoardType(BoardCategory boardCategory);

	@Operation(summary = "게시글 생성 API", description = "DTO를 작성해 게시글을 생성합니다.")
	@ApiResponse(
			responseCode = "201",
			description = "게시글 생성 성공 예시",
			content = @Content(mediaType = "application/json"))
	ResponseEntity<PostResponseDto> createPost(PostCreateRequestDto requestDto, Authentication auth);

	@Operation(summary = "단일 게시글 조회 API", description = "게시글 ID를 이용해 게시글을 가져옵니다.")
	@ApiResponse(
			responseCode = "200",
			description = "게시글 조회 성공 예시",
			content = {
				@Content(
						mediaType = "application/json",
						schema = @Schema(implementation = PostResponseDto.class))
			})
	ResponseEntity<PostResponseDto> getPost(@PathVariable(name = "id") Long id);

	@Operation(
			summary = "게시글 업데이트 API",
			description = "게시글 ID를 이용하고 DTO(회원ID가 포함된)를 작성해 게시글을 업데이트합니다.")
	@ApiResponse(
			responseCode = "200",
			description = "게시글 업데이트 성공 예시",
			content = {
				@Content(
						mediaType = "application/json",
						schema = @Schema(implementation = PostResponseDto.class))
			})
	ResponseEntity<PostResponseDto> updatePost(
			@PathVariable(name = "id") Long id, PostUpdateRequestDto request, Authentication auth);

	@Operation(summary = "게시글 삭제 API", description = "게시글 ID를 이용해 게시글을 삭제합니다.")
	@ApiResponse(
			responseCode = "200",
			description = "게시글 조회 성공 예시",
			content = {
				@Content(
						mediaType = "application/json",
						schema = @Schema(implementation = PostResponseDto.class))
			})
	ResponseEntity<Void> deletePost(@PathVariable(name = "id") Long id, Authentication auth);
}
