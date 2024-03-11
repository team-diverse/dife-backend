package com.diverse.dife.controller.community;

import com.diverse.dife.repository.community.ClipedHistoryRepository;
import com.diverse.dife.service.MemberService;
import com.diverse.dife.service.TranslationService;
import com.diverse.dife.service.community.ClipedHistoryService;
import com.diverse.dife.service.community.LikedHistoryService;
import com.diverse.dife.service.community.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
@Slf4j
public class PostController {

    private final PostService postService;
    private final MemberService memberService;
    private final LikedHistoryService likedHistoryService;
    private final ClipedHistoryService clipedHistoryService;
    private final TranslationService translationService;


}
