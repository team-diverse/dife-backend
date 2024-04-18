package com.dife.api.service;

import com.dife.api.exception.MemberNotFoundException;
import com.dife.api.model.Connect;
import com.dife.api.model.ConnectStatus;
import com.dife.api.model.Member;
import com.dife.api.model.dto.ConnectRequestDto;
import com.dife.api.model.dto.ConnectRequestDto;
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

    public void connectMembers(ConnectRequestDto dto, Member currentMember) {
        Member connectMember = memberRepository.findById(dto.getToMemberId())
                                .orElseThrow(MemberNotFoundException::new);

        Connect connect = new Connect();
        connect.setFromMember(currentMember);
        connect.setToMember(connectMember);
        connect.setStatus(ConnectStatus.PENDING);

        connectRepository.save(connect);
    }
}
