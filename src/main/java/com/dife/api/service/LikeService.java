package com.dife.api.service;

import static java.util.stream.Collectors.toList;

import com.dife.api.exception.*;
import com.dife.api.model.*;
import com.dife.api.model.dto.LikeCreateRequestDto;
import com.dife.api.model.dto.PostResponseDto;
import com.dife.api.repository.*;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class LikeService {

	private final PostRepository postRepository;
	private final CommentRepository commentRepository;
	private final MemberRepository memberRepository;
	private final LikePostRepository likePostRepository;
	private final LikeCommentRepository likeCommentRepository;
	private final LikeChatroomRepository likeChatroomRepository;
	private final ChatroomRepository chatroomRepository;

	private final ModelMapper modelMapper;
	private final NotificationService notificationService;
	private final PostService postService;

	public List<PostResponseDto> getLikedPosts(String memberEmail) {
		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		List<PostLike> postLikes = likePostRepository.findPostLikesByMember(member);

		List<Post> posts =
				postLikes.stream().map(PostLike::getPost).distinct().collect(Collectors.toList());

		return posts.stream().map(b -> postService.getPost(b.getId(), memberEmail)).collect(toList());
	}

	public void createLike(LikeCreateRequestDto dto, String memberEmail) {
		switch (dto.getType()) {
			case POST:
				createLikePost(dto.getId(), memberEmail);
				break;
			case COMMENT:
				createLikeComment(dto.getId(), memberEmail);
				break;
			case CHATROOM:
				createLikeChatroom(dto.getId(), memberEmail);
				break;
			case MEMBER:
				createLikeMember(dto.getId(), memberEmail);
				break;
		}
	}

	public void createLikePost(Long postId, String memberEmail) {
		Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		if (likePostRepository.existsByPostAndMember(post, member)) {
			throw new DuplicateLikeException();
		}
		PostLike postLike = new PostLike();
		postLike.setPost(post);
		postLike.setMember(member);
		likePostRepository.save(postLike);

		Member writer = post.getWriter();
		String message = "WOW!😆 " + member.getUsername() + "님이 회원님의 게시글을 좋아해요!";
		notificationService.addNotifications(writer, member, message, NotificationType.POST, postId);
	}

	public void createLikeComment(Long commentId, String memberEmail) {
		Comment comment =
				commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		if (likeCommentRepository.existsByCommentAndMember(comment, member)) {
			throw new DuplicateLikeException();
		}
		CommentLike commentLike = new CommentLike();
		commentLike.setComment(comment);
		commentLike.setMember(member);
		likeCommentRepository.save(commentLike);

		Member writer = comment.getWriter();
		String message = "WOW!😆 " + member.getUsername() + "님이 회원님의 댓글을 좋아해요!";
		notificationService.addNotifications(writer, member, message, NotificationType.POST, commentId);
	}

	public void createLikeChatroom(Long chatroomId, String memberEmail) {
		Chatroom chatroom =
				chatroomRepository.findById(chatroomId).orElseThrow(ChatroomNotFoundException::new);

		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		if (likeChatroomRepository.existsByChatroomAndMember(chatroom, member)) {
			throw new DuplicateLikeException();
		}
		ChatroomLike chatroomLike = new ChatroomLike();
		chatroomLike.setChatroom(chatroom);
		chatroomLike.setMember(member);

		likeChatroomRepository.save(chatroomLike);
	}

	public void createLikeMember(Long memberId, String memberEmail) {
		Member likeMember =
				memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);

		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		boolean isAlreadyLikelisted = false;

		List<Member> likeList = member.getLikeList();
		if (likeList != null) {
			isAlreadyLikelisted = likeList.stream().anyMatch(bl -> bl.equals(likeMember));
		}

		if (isAlreadyLikelisted) {
			throw new DuplicateMemberException("이미 좋아요리스트에 존재하는 회원입니다!");
		}

		member.getLikeList().add(likeMember);
	}

	public boolean isLikeListMember(Member currentMember, Member checkMember) {
		return currentMember.getLikeList().stream()
				.anyMatch(likelistedMember -> likelistedMember.equals(checkMember));
	}

	public void deleteLikePost(LikeCreateRequestDto dto, String memberEmail) {
		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		switch (dto.getType()) {
			case POST:
				Post post = postRepository.findById(dto.getId()).orElseThrow(PostNotFoundException::new);

				PostLike likePost =
						likePostRepository
								.findByPostAndMember(post, member)
								.orElseThrow(LikeNotFoundException::new);

				likePost.getPost().getPostLikes().remove(likePost);
				member.getPostLikes().remove(likePost);
				likePostRepository.delete(likePost);
				break;

			case COMMENT:
				Comment comment =
						commentRepository.findById(dto.getId()).orElseThrow(CommentNotFoundException::new);

				CommentLike commentLike =
						likeCommentRepository
								.findByCommentAndMember(comment, member)
								.orElseThrow(LikeNotFoundException::new);

				commentLike.getComment().getCommentLikes().remove(commentLike);
				likeCommentRepository.delete(commentLike);
				break;

			case CHATROOM:
				Chatroom chatroom =
						chatroomRepository.findById(dto.getId()).orElseThrow(ChatroomNotFoundException::new);

				ChatroomLike chatroomLike =
						likeChatroomRepository
								.findByChatroomAndMember(chatroom, member)
								.orElseThrow(LikeNotFoundException::new);

				likeChatroomRepository.delete(chatroomLike);
				break;

			case MEMBER:
				Member likedMember =
						memberRepository.findById(dto.getId()).orElseThrow(MemberNotFoundException::new);

				boolean isAlreadyLikelisted = false;

				List<Member> likeList = member.getLikeList();
				if (likeList != null) {
					isAlreadyLikelisted =
							likeList.stream().anyMatch(liked -> liked.getId().equals(likedMember.getId()));
				}

				if (!isAlreadyLikelisted) throw new LikeNotFoundException();

				member.getLikeList().remove(likedMember);
				memberRepository.save(member);
		}
	}
}
