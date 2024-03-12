package com.diverse.dife.service.chat;

import com.diverse.dife.repository.chat.ChatClipedHistoryRepository;
import com.diverse.dife.repository.chat.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
// 채팅 스크랩 서비스
public class ChatClipedService {

    private ChatClipedHistoryRepository chatClipedHistoryRepository;
}
