package com.dife.api.service;

import com.dife.api.exception.*;
import com.dife.api.model.*;
import com.dife.api.model.dto.ReportRequestDto;
import com.dife.api.model.dto.ReportResponseDto;
import com.dife.api.repository.*;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReportService {

	private final PostRepository postRepository;
	private final CommentRepository commentRepository;
	private final MemberRepository memberRepository;
	private final ReportRepository reportRepository;

	private final ModelMapper modelMapper;

	public ReportResponseDto createDeclaration(ReportRequestDto requestDto, String memberEmail) {

		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		Report report = new Report();
		report.setType(requestDto.getType());
		report.setMember(member);

		if (requestDto.getPostId() != null) {
			report.setPost(
					postRepository.findById(requestDto.getPostId()).orElseThrow(PostNotFoundException::new));
		} else if (requestDto.getCommentId() != null) {
			report.setComment(
					commentRepository
							.findById(requestDto.getCommentId())
							.orElseThrow(CommentNotFoundException::new));
		} else if (requestDto.getReceiverId() != null) {

			if (requestDto.getReceiverId() == member.getId()) throw new MemberSendingSelfException();
			report.setReceiver(
					memberRepository
							.findById(requestDto.getReceiverId())
							.orElseThrow(MemberNotFoundException::new));
		}

		Optional.ofNullable(requestDto.getMessage()).ifPresent(report::setDescription);

		reportRepository.save(report);

		return modelMapper.map(report, ReportResponseDto.class);
	}
}
