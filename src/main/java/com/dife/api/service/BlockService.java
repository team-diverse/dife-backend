package com.dife.api.service;

import com.dife.api.exception.*;
import com.dife.api.model.BlacklistedMember;
import com.dife.api.model.Member;
import com.dife.api.model.dto.BlockMemberRequestDto;
import com.dife.api.model.dto.MemberResponseDto;
import com.dife.api.repository.MemberRepository;
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
public class BlockService {

	private final MemberRepository memberRepository;

	private final ModelMapper modelMapper;

	public List<MemberResponseDto> createBlackList(
			BlockMemberRequestDto requestDto, String memberEmail) {
		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);
		Member blackMember =
				memberRepository
						.findById(requestDto.getBlockMemberId())
						.orElseThrow(MemberNotFoundException::new);

		if (member.getId().equals(requestDto.getBlockMemberId())) {
			throw new MemberSendingSelfException();
		}

		boolean isAlreadyBlacklisted =
				member.getBlackList().stream()
						.anyMatch(bl -> bl.getBlacklistedMember().equals(blackMember));

		if (isAlreadyBlacklisted) {
			throw new DuplicateMemberException("이미 블랙리스트에 존재하는 회원입니다!");
		}

		BlacklistedMember blacklistedMember = new BlacklistedMember();
		blacklistedMember.setBlacklistOwner(member);
		blacklistedMember.setBlacklistedMember(blackMember);

		member.getBlackList().add(blacklistedMember);
		memberRepository.save(member);

		return member.getBlackList().stream()
				.map(bl -> modelMapper.map(bl.getBlacklistedMember(), MemberResponseDto.class))
				.collect(Collectors.toList());
	}

	public List<MemberResponseDto> getBlackList(String memberEmail) {
		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		return member.getBlackList().stream()
				.map(bl -> modelMapper.map(bl.getBlacklistedMember(), MemberResponseDto.class))
				.collect(Collectors.toList());
	}
}
