package com.diverse.dife.controller.matching;

import com.diverse.dife.service.MemberService;
import com.diverse.dife.service.matching.GroupMatchingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/groupmatching")
@Slf4j
public class GroupMatchingController {

    private final GroupMatchingService groupMatchingService;
    private final MemberService memberService;

}
