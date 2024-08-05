package com.dife.api.service;

import com.dife.api.exception.*;
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

		boolean isAlreadyBlacklisted = false;

		List<Member> blackList = member.getBlackList();
		if (blackList != null) {
			isAlreadyBlacklisted = blackList.stream().anyMatch(bl -> bl.equals(blackMember));
		}

		if (isAlreadyBlacklisted) {
			throw new DuplicateMemberException("이미 블랙리스트에 존재하는 회원입니다!");
		}

		member.getBlackList().add(blackMember);

		memberRepository.save(member);

		return member.getBlackList().stream()
				.map(bl -> modelMapper.map(bl, MemberResponseDto.class))
				.collect(Collectors.toList());
	}

	public List<MemberResponseDto> getBlackList(String memberEmail) {
		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		return member.getBlackList().stream()
				.map(bl -> modelMapper.map(bl, MemberResponseDto.class))
				.collect(Collectors.toList());
	}

	public boolean isBlackListMember(Member currentMember, Member checkMember) {
		return currentMember.getBlackList().stream()
				.anyMatch(blacklistedMember -> blacklistedMember.equals(checkMember));
	}
}
