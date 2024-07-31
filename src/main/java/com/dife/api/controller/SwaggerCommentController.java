package com.dife.api.controller;

import com.dife.api.model.dto.CommentCreateRequestDto;
import com.dife.api.model.dto.CommentResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Comment API", description = "댓글 서비스 API")
public interface SwaggerCommentController {

	@Operation(
			summary = "댓글 조회 API",
			description = "게시글 id를 입력해 속한 댓글을 조회하는 API입니다. 추가로 좋아요를 누른 댓글인지 여부도 표시됩니다.")
	@ApiResponse(
			responseCode = "200",
			description = "단일 댓글 조회 예시",
			content = {
				@Content(
						mediaType = "application/json",
						schema = @Schema(implementation = CommentResponseDto.class))
			})
	ResponseEntity<List<CommentResponseDto>> getCommentsByPostId(
			@PathVariable(name = "postId") Long postId, Authentication auth);

	@Operation(summary = "댓글 생성 API", description = "사용자가 DTO를 작성해 게시글에 대한 댓글을 생성하는 API입니다.")
	@ApiResponse(
			responseCode = "201",
			description = "댓글 생성 성공 예시",
			content = {
				@Content(
						mediaType = "application/json",
						schema = @Schema(implementation = CommentResponseDto.class))
			})
	ResponseEntity<CommentResponseDto> createComment(
			CommentCreateRequestDto requestDto, Authentication auth);
}
