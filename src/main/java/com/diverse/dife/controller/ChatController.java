package com.diverse.dife.controller;


import com.diverse.dife.service.ChatService;
import com.diverse.dife.service.TranslationService;
import com.diverse.dife.service.community.ClipedHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
@Slf4j
public class ChatController {

    private final ChatService chatService;
    private final ClipedHistoryService clipedHistoryService;
    private final TranslationService translationService;

}
