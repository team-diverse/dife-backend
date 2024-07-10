package com.dife.api.service;

import static java.util.stream.Collectors.toList;

import com.dife.api.exception.*;
import com.dife.api.model.Connect;
import com.dife.api.model.ConnectStatus;
import com.dife.api.model.Member;
import com.dife.api.model.dto.ConnectPatchRequestDto;
import com.dife.api.model.dto.ConnectRequestDto;
import com.dife.api.model.dto.ConnectResponseDto;
import com.dife.api.repository.ConnectRepository;
import com.dife.api.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ConnectService {
	private final ConnectRepository connectRepository;
	private final MemberRepository memberRepository;

	private final ModelMapper modelMapper;

	@Transactional(readOnly = true)
	public List<ConnectResponseDto> getConnects(String currentMemberEmail) {
		Member currentMember =
				memberRepository.findByEmail(currentMemberEmail).orElseThrow(MemberNotFoundException::new);
		List<Connect> connects =
				connectRepository.findAllByMemberAndStatus(currentMember, ConnectStatus.ACCEPTED);

		return connects.stream()
				.map(c -> modelMapper.map(c, ConnectResponseDto.class))
				.collect(toList());
	}

	@Transactional(readOnly = true)
	public ConnectResponseDto getConnect(Long memberId, String currentMemberEmail) {
		Member currentMember =
				memberRepository.findByEmail(currentMemberEmail).orElseThrow(MemberNotFoundException::new);
		Member otherMember =
				memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);

		Connect connect =
				connectRepository
						.findByMemberPair(otherMember, currentMember)
						.orElseThrow(ConnectNotFoundException::new);

		return modelMapper.map(connect, ConnectResponseDto.class);
	}

	public ConnectResponseDto saveConnect(ConnectRequestDto dto, String currentMemberEmail) {
		Member currentMember =
				memberRepository.findByEmail(currentMemberEmail).orElseThrow(MemberNotFoundException::new);
		Member toMember =
				memberRepository.findById(dto.getToMemberId()).orElseThrow(MemberNotFoundException::new);

		if (currentMember.getId().equals(toMember.getId())) {
			throw new IdenticalConnectException();
		}
		if (!connectRepository.findByMemberPair(currentMember, toMember).isEmpty()) {
			throw new ConnectDuplicateException();
		}

		Connect connect = new Connect();
		connect.setFromMember(currentMember);
		connect.setToMember(toMember);
		connect.setStatus(ConnectStatus.PENDING);
		connectRepository.save(connect);

		return modelMapper.map(connect, ConnectResponseDto.class);
	}

	public void acceptConnect(ConnectPatchRequestDto requestDto, String currentMemberEmail) {
		Member currentMember =
				memberRepository.findByEmail(currentMemberEmail).orElseThrow(MemberNotFoundException::new);
		Member otherMember =
				memberRepository
						.findById(requestDto.getMemberId())
						.orElseThrow(MemberNotFoundException::new);

		Connect connect =
				connectRepository
						.findByFromMemberAndToMember(otherMember, currentMember)
						.orElseThrow(ConnectNotFoundException::new);
		connect.setStatus(ConnectStatus.ACCEPTED);
	}

	public void deleteConnect(Long id, String email) {
		if (!isConnectRelevant(id, email)) {
			throw new ConnectUnauthorizedException();
		}
		connectRepository.deleteById(id);
	}

	public boolean isConnectRelevant(Long connectId, String email) {
		Connect connect =
				connectRepository.findById(connectId).orElseThrow(ConnectNotFoundException::new);
		String fromMemberEmail = connect.getFromMember().getEmail();
		String toMemberEmail = connect.getToMember().getEmail();
		return fromMemberEmail.equals(email) || toMemberEmail.equals(email);
	}
}
