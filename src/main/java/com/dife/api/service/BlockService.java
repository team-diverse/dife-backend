package com.dife.api.service;

import com.dife.api.exception.*;
import com.dife.api.model.Member;
import com.dife.api.model.MemberBlock;
import com.dife.api.model.dto.BlockMemberRequestDto;
import com.dife.api.model.dto.BlockMemberResponseDto;
import com.dife.api.repository.BlockMemberRepository;
import com.dife.api.repository.MemberRepository;
import java.util.List;
import java.util.Set;
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
	private final BlockMemberRepository blockMemberRepository;

	private final ModelMapper modelMapper;

	public List<BlockMemberResponseDto> createBlackList(
			BlockMemberRequestDto requestDto, String memberEmail) {
		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);
		Member blackMember =
				memberRepository
						.findById(requestDto.getMemberId())
						.orElseThrow(MemberNotFoundException::new);

		if (member.getId().equals(requestDto.getMemberId())) {
			throw new MemberSendingSelfException();
		}

		boolean isAlreadyBlacklisted = false;

		Set<MemberBlock> blackList = member.getBlackList();

		if (blackList != null) {
			isAlreadyBlacklisted =
					blackList.stream()
							.anyMatch(bl -> bl.getBlacklistedMember().getId().equals(blackMember.getId()));
		}

		if (isAlreadyBlacklisted) {
			throw new DuplicateMemberException("이미 블랙리스트에 존재하는 회원입니다!");
		}

		MemberBlock memberBlock = new MemberBlock();
		memberBlock.setMember(member);
		memberBlock.setBlacklistedMember(blackMember);

		blockMemberRepository.save(memberBlock);

		member.getBlackList().add(memberBlock);
		memberRepository.save(member);

		return member.getBlackList().stream()
				.map(bl -> modelMapper.map(bl, BlockMemberResponseDto.class))
				.collect(Collectors.toList());
	}

	public List<BlockMemberResponseDto> getBlackList(String memberEmail) {
		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		return member.getBlackList().stream()
				.map(bl -> modelMapper.map(bl, BlockMemberResponseDto.class))
				.collect(Collectors.toList());
	}

	public Set<Member> getBlackSet(Member member) {
		Set<MemberBlock> blockedMemberBlocks = member.getBlackList();
		return blockedMemberBlocks.stream()
				.map(MemberBlock::getBlacklistedMember)
				.collect(Collectors.toSet());
	}

	public boolean isBlackListMember(Member currentMember, Member checkMember) {
		return currentMember.getBlackList().stream()
				.anyMatch(blacklistedMember -> blacklistedMember.equals(checkMember));
	}

	public void deleteBlock(Long memberId, String memberEmail) {
		Member currentMember =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);

		Member blockMember =
				memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);

		MemberBlock blockToRemove =
				blockMemberRepository
						.findByMemberAndBlacklistedMember(currentMember, blockMember)
						.orElseThrow(BlockNotFoundException::new);

		currentMember.getBlackList().remove(blockToRemove);
		blockMember.getBlackList().removeIf(block -> block.getMember().equals(currentMember));

		blockMemberRepository.delete(blockToRemove);

		memberRepository.save(currentMember);
		memberRepository.save(blockMember);
	}
}
