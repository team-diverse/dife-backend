package com.dife.api.service;

import com.dife.api.exception.BookmarkNotFoundException;
import com.dife.api.model.Bookmark;
import com.dife.api.model.Chat;
import com.dife.api.model.Chatroom;
import com.dife.api.model.Member;
import com.dife.api.model.dto.BookmarkDto;
import com.dife.api.repository.BookmarkRepository;
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
	private final MemberService memberService;

	public Bookmark createBookmark(Long room_id, Long chat_id, String memberEmail) {

		Member member = memberService.getMember(memberEmail);
		Chatroom chatroom = chatroomService.getChatroom(room_id);
		Chat chat = chatroomService.getChat(room_id, chat_id);

		Bookmark bookmark = new Bookmark();
		bookmark.setMessage(chat.getMessage());
		bookmark.setMember(member);
		bookmark.setChatroom(chatroom);
		bookmarkRepository.save(bookmark);

		return bookmark;
	}

	public List<BookmarkDto> getBookmarks(String email) {

		List<Bookmark> bookmarks = bookmarkRepository.findBookmarksByMemberEmail(email);

		return bookmarks.stream()
				.map(bookmark -> new BookmarkDto(bookmark))
				.collect(Collectors.toList());
	}

	public Bookmark getBookmark(Long id, String email) {
		Bookmark bookmark =
				bookmarkRepository
						.findByMemberEmailAndId(id, email)
						.orElseThrow(() -> new BookmarkNotFoundException());
		return bookmark;
	}
}
