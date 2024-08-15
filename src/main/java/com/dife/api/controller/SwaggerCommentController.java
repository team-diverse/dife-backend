package com.dife.api.controller;

import com.dife.api.model.dto.CommentCreateRequestDto;
import com.dife.api.model.dto.CommentResponseDto;
import com.dife.api.model.dto.PostResponseDto;
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

	@Operation(summary = "게시글 댓글 리스트 조회 API", description = "게시글 ID를 이용해 댓글 리스트를 가져옵니다.")
	@ApiResponse(
			responseCode = "200",
			description = "게시글 조회 성공 예시",
			content = {
				@Content(
						mediaType = "application/json",
						schema = @Schema(implementation = PostResponseDto.class))
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

	@Operation(summary = "댓글 삭제 API", description = "댓글 ID를 이용해 댓글을 삭제합니다.")
	@ApiResponse(responseCode = "200")
	ResponseEntity<Void> deletePost(@PathVariable(name = "id") Long id, Authentication auth);
}
