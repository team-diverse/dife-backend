package com.dife.api.controller;

import com.dife.api.model.Bookmark;
import com.dife.api.model.dto.BookmarkDto;
import com.dife.api.service.BookmarkService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
			@RequestParam("sender") String sender) {
		Bookmark bookmark = bookmarkService.createBookmark(room_id, chat_id, sender);
		return ResponseEntity.ok(new BookmarkDto(bookmark));
	}

	@GetMapping
	public ResponseEntity<List<BookmarkDto>> getBookmarks(
			@RequestParam(name = "room_id") Long room_id) {
		List<BookmarkDto> bookmarks = bookmarkService.getBookmarks(room_id);
		return ResponseEntity.ok(bookmarks);
	}

	@GetMapping("/detail")
	public ResponseEntity<BookmarkDto> getBookmark(
			@RequestParam(name = "room_id") Long room_id,
			@RequestParam(name = "bookmark_id") Long bookmark_id) {
		Bookmark bookmark = bookmarkService.getBookmark(room_id, bookmark_id);
		return ResponseEntity.ok(new BookmarkDto(bookmark));
	}
}
