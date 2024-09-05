package com.dife.api.service;

import com.dife.api.exception.*;
import com.dife.api.model.*;
import com.dife.api.model.dto.*;
import com.dife.api.repository.*;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReportService {

	private final PostRepository postRepository;
	private final CommentRepository commentRepository;
	private final ChatroomRepository chatroomRepository;
	private final MemberRepository memberRepository;
	private final ReportRepository reportRepository;

	public void createReport(ReportRequestDto requestDto, String memberEmail) throws IOException {

		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		Report report = new Report();
		report.setType(requestDto.getType());
		report.setMember(member);

		if (requestDto.getPostId() != null) {

			Post post =
					postRepository.findById(requestDto.getPostId()).orElseThrow(PostNotFoundException::new);
			report.setPost(post);
		} else if (requestDto.getCommentId() != null) {

			Comment comment =
					commentRepository
							.findById(requestDto.getCommentId())
							.orElseThrow(CommentNotFoundException::new);

			report.setComment(comment);
		} else if (requestDto.getReceiverId() != null) {

			if (requestDto.getReceiverId() == member.getId()) throw new MemberSendingSelfException();

			Member receiver =
					memberRepository
							.findById(requestDto.getReceiverId())
							.orElseThrow(MemberNotFoundException::new);

			report.setReceiver(receiver);
		} else if (requestDto.getChatroomId() != null) {

			Chatroom chatroom =
					chatroomRepository
							.findById(requestDto.getChatroomId())
							.orElseThrow(ChatroomNotFoundException::new);

			report.setChatroom(chatroom);
		}

		Optional.ofNullable(requestDto.getMessage()).ifPresent(report::setDescription);

		reportRepository.save(report);
	}
}
