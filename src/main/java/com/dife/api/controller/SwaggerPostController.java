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
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Post API", description = "게시판/게시글 서비스 API")
public interface SwaggerPostController {

	@Operation(summary = "게시판 조회 API", description = "게시판 종류를 입력해 최신순으로 게시판을 조회합니다.")
	@ApiResponse(responseCode = "200")
	ResponseEntity<List<PostResponseDto>> getPostsByBoardType(
			@RequestParam(name = "type") BoardCategory type, Authentication auth);

	@Operation(
			summary = "게시글 생성 API",
			description =
					"DTO를 작성해 게시글을 생성합니다. \n 만약 알림 전송 문제가 있다는 500에러가 뜬다면 /notifications/push API를 통해 알림 토큰인 pushToken을 생성했는지 확인해주세요!")
	@ApiResponse(
			responseCode = "201",
			description = "게시글 생성 성공 예시",
			content = @Content(mediaType = "application/json"))
	ResponseEntity<PostResponseDto> createPost(
			@RequestParam(name = "title") String title,
			@RequestParam(name = "content") String content,
			@RequestParam(name = "isPublic") Boolean isPublic,
			@RequestParam(name = "boardType") BoardCategory boardType,
			@RequestParam(name = "postFiles", required = false) List<MultipartFile> postFiles,
			Authentication auth);

	@Operation(
			summary = "단일 게시글 조회 API",
			description = "게시글 ID를 이용해 게시글을 가져옵니다. 추가로 좋아요를 누른 게시물인지 여부도 표시됩니다.")
	@ApiResponse(
			responseCode = "200",
			description = "게시글 조회 성공 예시",
			content = {
				@Content(
						mediaType = "application/json",
						schema = @Schema(implementation = PostResponseDto.class))
			})
	ResponseEntity<PostResponseDto> getPost(@PathVariable(name = "id") Long id, Authentication auth);

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
			@PathVariable(name = "id") Long id,
			@RequestParam(name = "title") String title,
			@RequestParam(name = "content") String content,
			@RequestParam(name = "isPublic") Boolean isPublic,
			@RequestParam(name = "boardType") BoardCategory boardType,
			@RequestParam(name = "postFiles", required = false) List<MultipartFile> postFiles,
			Authentication auth);

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

	@Operation(summary = "게시글 차단 API", description = "게시글 ID를 이용해 특정 게시글을 차단합니다.")
	@ApiResponse(responseCode = "201")
	ResponseEntity<Void> createBlock(@PathVariable(name = "postId") Long postId, Authentication auth);

	@Operation(
			summary = "게시판 검색 조회 API",
			description = "게시글의 title, content를 바탕으로 게시판을 조회합니다. 게시판 type 또한 입력으로 받아 구체적인 검색을 허용합니다.")
	@ApiResponse(responseCode = "200")
	ResponseEntity<List<PostResponseDto>> getSearchedPosts(
			@RequestParam(name = "keyword", required = false) String keyword,
			@RequestParam(name = "type", required = false) BoardCategory boardCategory,
			Authentication auth);
}
