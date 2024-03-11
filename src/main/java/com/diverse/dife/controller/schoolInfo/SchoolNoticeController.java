package com.diverse.dife.controller.schoolInfo;

import com.diverse.dife.service.TranslationService;
import com.diverse.dife.service.schoolInfo.SchoolNoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schoolnotice")
@Slf4j
public class SchoolNoticeController {

    private final SchoolNoticeService schoolNoticeService;
    private final TranslationService translationService;
}
