package com.dife.api.controller;

import com.dife.api.model.Bookmark;
import com.dife.api.model.dto.BookmarkDto;
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
@RequestMapping("/api/bookmark")
@Slf4j
public class BookmarkController {

	private final BookmarkService bookmarkService;

	@PostMapping
	public ResponseEntity<BookmarkDto> createBookmark(
			@RequestParam(name = "room_id") Long room_id,
			@RequestParam(name = "chat_id") Long chat_id,
			Authentication auth) {
		Bookmark bookmark = bookmarkService.createBookmark(room_id, chat_id, auth.getName());
		return ResponseEntity.ok(new BookmarkDto(bookmark));
	}

	@GetMapping
	public ResponseEntity<List<BookmarkDto>> getBookmarks(Authentication auth) {
		List<BookmarkDto> bookmarks = bookmarkService.getBookmarks(auth.getName());
		return ResponseEntity.ok(bookmarks);
	}

	@GetMapping("/detail")
	public ResponseEntity<BookmarkDto> getBookmark(
			@PathVariable(name = "id") Long id, Authentication auth) {
		Bookmark bookmark = bookmarkService.getBookmark(id, auth.getName());
		return ResponseEntity.ok(new BookmarkDto(bookmark));
	}
}
