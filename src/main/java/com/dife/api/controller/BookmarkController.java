package com.dife.api.controller;

import com.dife.api.model.dto.BookmarkPostRequestDto;
import com.dife.api.model.dto.BookmarkResponseDto;
import com.dife.api.model.dto.BookmarksGetByChatroomRequestDto;
import com.dife.api.service.BookmarkService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/bookmarks")
@Slf4j
public class BookmarkController {

	private final BookmarkService bookmarkService;

	@PostMapping("/")
	public ResponseEntity<BookmarkResponseDto> createBookmark(
			BookmarkPostRequestDto requestDto, Authentication auth) {
		BookmarkResponseDto responseDto = bookmarkService.createBookmark(requestDto, auth.getName());
		return ResponseEntity.ok(responseDto);
	}

	@GetMapping("/")
	public ResponseEntity<List<BookmarkResponseDto>> getBookmarks(
			BookmarksGetByChatroomRequestDto requestDto, Authentication authentication) {
		List<BookmarkResponseDto> bookmarks =
				bookmarkService.getBookmarks(requestDto, authentication.getName());
		return ResponseEntity.ok(bookmarks);
	}
}
