package com.dife.api.service;

import com.dife.api.exception.*;
import com.dife.api.model.Connect;
import com.dife.api.model.ConnectStatus;
import com.dife.api.model.Member;
import com.dife.api.model.dto.ConnectPatchRequestDto;
import com.dife.api.model.dto.ConnectRequestDto;
import com.dife.api.model.dto.ConnectResponseDto;
import com.dife.api.repository.ConnectRepository;
import com.dife.api.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ConnectService {
    private final ConnectRepository connectRepository;
    private final MemberRepository memberRepository;

    private final ModelMapper modelMapper;

    public ConnectResponseDto saveConnect(ConnectRequestDto dto, String currentMemberEmail) {
        Member currentMember = memberRepository.findByEmail(currentMemberEmail).orElseThrow(MemberNotFoundException::new);
        Member toMember = memberRepository.findById(dto.getToMemberId()).orElseThrow(MemberNotFoundException::new);

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
        if (!isConnectRelevant(requestDto.getMemberId(), currentMemberEmail)) {
            throw new ConnectUnauthorizedException();
        }
        Member currentMember = memberRepository.findByEmail(currentMemberEmail).orElseThrow(MemberNotFoundException::new);
        Member otherMember = memberRepository.findById(requestDto.getMemberId()).orElseThrow(MemberNotFoundException::new);

        Connect connect = connectRepository.findByFromMemberAndToMember(otherMember, currentMember).orElseThrow(ConnectNotFoundException::new);
        connect.setStatus(ConnectStatus.ACCEPTED);
    }

    public boolean isConnectRelevant(Long id, String email) {
        Connect connect = connectRepository.findById(id).orElseThrow(ConnectNotFoundException::new);
        String fromMemberEmail = connect.getFromMember().getEmail();
        String toMemberEmail = connect.getToMember().getEmail();
        return fromMemberEmail.equals(email) || toMemberEmail.equals(email);
    }
}
