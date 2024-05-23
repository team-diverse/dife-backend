package com.dife.api.service;

import com.dife.api.exception.BookmarkNotFoundException;
import com.dife.api.exception.ChatroomException;
import com.dife.api.model.Bookmark;
import com.dife.api.model.Chat;
import com.dife.api.model.Chatroom;
import com.dife.api.model.Member;
import com.dife.api.model.dto.BookmarkDto;
import com.dife.api.repository.BookmarkRepository;
import com.dife.api.repository.ChatRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BookmarkService {

	private final BookmarkRepository bookmarkRepository;
	private final ChatroomService chatroomService;
	private final ChatRepository chatRepository;
	private final MemberService memberService;

	public Bookmark createBookmark(Long room_id, Long chat_id, String memberEmail) {

		Member member = memberService.getMember(memberEmail);
		Chatroom chatroom = chatroomService.getChatroom(room_id);
		Chat chat =
				chatRepository
						.findByChatroomIdAndId(room_id, chat_id)
						.orElseThrow(() -> new ChatroomException("유효하지 않은 채팅입니다!"));

		Bookmark bookmark = new Bookmark();
		bookmark.setMessage(chat.getMessage());
		bookmark.setMember(member);
		bookmark.setChatroom(chatroom);
		bookmarkRepository.save(bookmark);

		return bookmark;
	}

	public List<BookmarkDto> getBookmarks(Long id) {

		List<Bookmark> bookmarks = bookmarkRepository.findScrapsByChatroomId(id);

		return bookmarks.stream()
				.map(bookmark -> new BookmarkDto(bookmark))
				.collect(Collectors.toList());
	}

	public Bookmark getBookmark(Long room_id, Long bookmark_id) {
		Bookmark bookmark =
				bookmarkRepository
						.findByChatroomIdAndId(room_id, bookmark_id)
						.orElseThrow(() -> new BookmarkNotFoundException());
		return bookmark;
	}
}
