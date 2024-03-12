package com.diverse.dife.service;


import com.diverse.dife.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
// 설정 서비스 -> 개인 설정만 담을 건지 후에 추가 논의
public class SettingService {

    private final MemberRepository memberRepository;
}
