package com.dife.api.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.dife.api.model.dto.BookmarkCreateRequestDto;
import com.dife.api.model.dto.BookmarkResponseDto;
import com.dife.api.service.BookmarkService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/bookmarks")
@Slf4j
public class BookmarkController implements SwaggerBookmarkController {

	private final BookmarkService bookmarkService;

	@GetMapping
	public ResponseEntity<List<BookmarkResponseDto>> getAllBookmarks(Authentication authentication) {
		List<BookmarkResponseDto> bookmarks = bookmarkService.getAllBookmarks(authentication.getName());
		return ResponseEntity.ok(bookmarks);
	}

	@PostMapping
	public ResponseEntity<BookmarkResponseDto> createBookmark(
			@RequestBody BookmarkCreateRequestDto requestDto, Authentication auth) {
		BookmarkResponseDto responseDto = bookmarkService.createBookmark(requestDto, auth.getName());
		return ResponseEntity.status(CREATED).body(responseDto);
	}

	@GetMapping("/{chatroomId}")
	public ResponseEntity<List<BookmarkResponseDto>> getBookmarkChats(
			@PathVariable(name = "chatroomId") Long chatroomId, Authentication authentication) {
		List<BookmarkResponseDto> bookmarks =
				bookmarkService.getChatroomBookmarks(chatroomId, authentication.getName());
		return ResponseEntity.ok(bookmarks);
	}

	@DeleteMapping
	public ResponseEntity<Void> deleteBookmarkPost(
			@RequestBody BookmarkCreateRequestDto requestDto, Authentication auth) {
		bookmarkService.deleteBookmark(requestDto, auth.getName());
		return new ResponseEntity<>(OK);
	}
}
