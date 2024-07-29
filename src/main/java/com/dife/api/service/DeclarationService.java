package com.dife.api.service;

import com.dife.api.exception.*;
import com.dife.api.model.*;
import com.dife.api.model.dto.DeclarationRequestDto;
import com.dife.api.model.dto.DeclarationResponseDto;
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
public class DeclarationService {

	private final PostRepository postRepository;
	private final CommentRepository commentRepository;
	private final MemberRepository memberRepository;
	private final DeclarationRepository declarationRepository;

	private final ModelMapper modelMapper;

	public DeclarationResponseDto createDeclaration(
			DeclarationRequestDto requestDto, String memberEmail) {

		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		Declaration declaration = new Declaration();
		declaration.setType(requestDto.getType());
		declaration.setMember(member);

		if (requestDto.getPostId() != null) {
			declaration.setPost(
					postRepository.findById(requestDto.getPostId()).orElseThrow(PostNotFoundException::new));
		} else if (requestDto.getCommentId() != null) {
			declaration.setComment(
					commentRepository
							.findById(requestDto.getCommentId())
							.orElseThrow(CommentNotFoundException::new));
		} else if (requestDto.getReceiverId() != null) {

			if (requestDto.getReceiverId() == member.getId()) throw new MemberSendingSelfException();
			declaration.setReceiver(
					memberRepository
							.findById(requestDto.getReceiverId())
							.orElseThrow(MemberNotFoundException::new));
		}

		Optional.ofNullable(requestDto.getMessage()).ifPresent(declaration::setMessage);

		declarationRepository.save(declaration);

		return modelMapper.map(declaration, DeclarationResponseDto.class);
	}
}
