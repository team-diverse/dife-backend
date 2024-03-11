package com.diverse.dife.controller.community;

import com.diverse.dife.service.TranslationService;
import com.diverse.dife.service.community.CommentService;
import com.diverse.dife.service.community.LikedHistoryService;
import com.diverse.dife.service.community.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final PostService postService;
    private final LikedHistoryService likedHistoryService;
    private final TranslationService translationService;

    private final CommentService commentService;

}
