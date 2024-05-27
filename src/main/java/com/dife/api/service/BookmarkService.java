package com.dife.api.service;

import static java.util.stream.Collectors.toList;

import com.dife.api.exception.BookmarkNotFoundException;
import com.dife.api.exception.ChatroomException;
import com.dife.api.exception.ChatroomNotFoundException;
import com.dife.api.exception.MemberNotFoundException;
import com.dife.api.model.Bookmark;
import com.dife.api.model.Chat;
import com.dife.api.model.Chatroom;
import com.dife.api.model.Member;
import com.dife.api.model.dto.BookmarkPostRequestDto;
import com.dife.api.model.dto.BookmarkResponseDto;
import com.dife.api.model.dto.BookmarksGetByChatroomRequestDto;
import com.dife.api.repository.BookmarkRepository;
import com.dife.api.repository.ChatRepository;
import com.dife.api.repository.ChatroomRepository;
import com.dife.api.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BookmarkService {

	private final BookmarkRepository bookmarkRepository;
	private final ChatroomRepository chatroomRepository;
	private final ChatRepository chatRepository;
	private final MemberRepository memberRepository;
	private final ModelMapper modelMapper;

	public List<BookmarkResponseDto> getBookmarks(
			BookmarksGetByChatroomRequestDto requestDto, String memberEmail) {

		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		Chatroom chatroom =
				chatroomRepository
						.findById(requestDto.getChatroomId())
						.orElseThrow(ChatroomNotFoundException::new);

		if (chatroom.getMembers().contains(member)) {
			List<Bookmark> bookmarks =
					bookmarkRepository.findScrapsByChatroomId(requestDto.getChatroomId());

			return bookmarks.stream()
					.map(b -> modelMapper.map(b, BookmarkResponseDto.class))
					.collect(toList());
		}
		throw new BookmarkNotFoundException();
	}

	public BookmarkResponseDto createBookmark(BookmarkPostRequestDto requestDto, String memberEmail) {

		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);
		Chatroom chatroom =
				chatroomRepository
						.findById(requestDto.getChatroomId())
						.orElseThrow(ChatroomNotFoundException::new);
		Chat chat =
				chatRepository
						.findByChatroomIdAndId(requestDto.getChatroomId(), requestDto.getChatId())
						.orElseThrow(() -> new ChatroomException("유효하지 않은 채팅입니다!"));

		Bookmark bookmark = new Bookmark();
		bookmark.setMessage(chat.getMessage());
		bookmark.setMember(member);
		bookmark.setChatroom(chatroom);
		bookmarkRepository.save(bookmark);

		return modelMapper.map(bookmark, BookmarkResponseDto.class);
	}
}
