package com.dife.api.service;

import static java.util.stream.Collectors.toList;

import com.dife.api.exception.*;
import com.dife.api.model.*;
import com.dife.api.model.dto.BookmarkCreateRequestDto;
import com.dife.api.model.dto.BookmarkResponseDto;
import com.dife.api.repository.*;
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
	private final PostRepository postRepository;
	private final MemberRepository memberRepository;
	private final ModelMapper modelMapper;

	private final PostService postService;

	public List<BookmarkResponseDto> getAllBookmarks(String memberEmail) {

		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);
		List<Bookmark> bookmarks = bookmarkRepository.findAllByMember(member);

		return bookmarks.stream()
				.map(
						b -> {
							BookmarkResponseDto responseDto = modelMapper.map(b, BookmarkResponseDto.class);
							if (b.getMessage() == null)
								responseDto.setPost(postService.getPost(b.getPost().getId(), memberEmail));
							return responseDto;
						})
				.collect(toList());
	}

	public List<BookmarkResponseDto> getChatroomBookmarks(Long chatroomId, String memberEmail) {

		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		Chatroom chatroom =
				chatroomRepository.findById(chatroomId).orElseThrow(ChatroomNotFoundException::new);

		if (chatroom.getMembers().contains(member)) {
			List<Bookmark> bookmarks =
					bookmarkRepository.findBookmarksByMemberAndChatroomId(chatroomId, member);

			return bookmarks.stream()
					.map(b -> modelMapper.map(b, BookmarkResponseDto.class))
					.collect(toList());
		}
		throw new BookmarkNotFoundException();
	}

	public BookmarkResponseDto createBookmark(
			BookmarkCreateRequestDto requestDto, String memberEmail) {

		switch (requestDto.getType()) {
			case POST:
				return createBookmarkPost(requestDto, memberEmail);
			default:
				return createBookmarkChat(requestDto, memberEmail);
		}
	}

	public BookmarkResponseDto createBookmarkChat(
			BookmarkCreateRequestDto requestDto, String memberEmail) {

		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);
		Chat chat =
				chatRepository
						.findByChatroomIdAndId(requestDto.getChatroomId(), requestDto.getChatId())
						.orElseThrow(() -> new ChatroomException("유효하지 않은 채팅입니다!"));

		if (bookmarkRepository.existsBookmarkByMessage(chat.getMessage()))
			throw new DuplicateBookmarkException();
		Bookmark bookmark = new Bookmark();
		bookmark.setMessage(chat.getMessage());
		bookmark.setMember(member);
		bookmarkRepository.save(bookmark);

		return modelMapper.map(bookmark, BookmarkResponseDto.class);
	}

	public BookmarkResponseDto createBookmarkPost(
			BookmarkCreateRequestDto requestDto, String memberEmail) {

		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);
		Post post =
				postRepository.findById(requestDto.getPostId()).orElseThrow(PostNotFoundException::new);
		if (!post.getIsPublic()) throw new PostUnauthorizedException();

		if (bookmarkRepository.existsBookmarkByPostAndMember(post, member))
			throw new DuplicateBookmarkException();

		Bookmark bookmark = new Bookmark();
		bookmark.setPost(post);
		bookmark.setMember(member);
		bookmarkRepository.save(bookmark);

		BookmarkResponseDto responseDto = modelMapper.map(bookmark, BookmarkResponseDto.class);
		responseDto.setPost(postService.getPost(post.getId(), memberEmail));
		return responseDto;
	}

	public void deleteBookmark(BookmarkCreateRequestDto requestDto, String memberEmail) {
		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		switch (requestDto.getType()) {
			case POST:
				Post post =
						postRepository.findById(requestDto.getPostId()).orElseThrow(PostNotFoundException::new);

				Bookmark bookmarkPost =
						bookmarkRepository
								.findBookmarkByPostAndMember(post, member)
								.orElseThrow(PostNotFoundException::new);

				bookmarkPost.getPost().getBookmarks().remove(bookmarkPost);
				member.getBookmarks().remove(bookmarkPost);
				bookmarkRepository.delete(bookmarkPost);
				break;
		}
	}
}
