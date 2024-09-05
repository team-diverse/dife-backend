package com.dife.api.controller;

import com.dife.api.model.dto.LikeCreateRequestDto;
import com.dife.api.model.dto.PostResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

@Tag(name = "Comment API", description = "댓글 서비스 API")
public interface SwaggerLikeController {

	@Operation(
			summary = "좋아요한 게시글 조회 API",
			description = "회원 Id를 입력해 회원이 좋아요를 누른 게시글 리스트를 조회하는 API입니다.")
	@ApiResponse(
			responseCode = "200",
			description = "단일 좋아요 게시글 조회 예시",
			content = {
				@Content(
						mediaType = "application/json",
						schema = @Schema(implementation = PostResponseDto.class))
			})
	ResponseEntity<List<PostResponseDto>> getLikedPosts(Authentication auth);

	@Operation(summary = "게시글/댓글 좋아요 생성 API", description = "사용자가 DTO를 작성해 게시글/댓글 좋아요를 생성하는 API입니다.")
	@ApiResponse(responseCode = "201")
	ResponseEntity<Void> createLike(LikeCreateRequestDto requestDto, Authentication auth);

	@Operation(summary = "게시글/댓글 좋아요 취소 API", description = "사용자가 DTO를 작성해 게시글/댓글 좋아요를 취소하는 API입니다.")
	@ApiResponse(responseCode = "200")
	ResponseEntity<Void> deleteLikePost(LikeCreateRequestDto requestDto, Authentication auth);
}
