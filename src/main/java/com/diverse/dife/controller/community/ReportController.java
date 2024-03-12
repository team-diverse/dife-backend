package com.diverse.dife.controller.community;

import com.diverse.dife.service.MemberService;
import com.diverse.dife.service.TranslationService;
import com.diverse.dife.service.community.CommentService;
import com.diverse.dife.service.community.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/report")
@Slf4j
public class ReportController {

    private final PostService postService;
    private final CommentService commentService;
    private final MemberService memberService;
    private final TranslationService translationService;
}
