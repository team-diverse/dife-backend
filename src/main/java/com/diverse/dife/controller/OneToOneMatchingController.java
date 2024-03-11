package com.diverse.dife.controller;

import com.diverse.dife.service.MemberService;
import com.diverse.dife.service.matching.OneToOneMatchingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/onetoonematching")
@Slf4j
public class OneToOneMatchingController {

    private final OneToOneMatchingService oneToOneMatchingService;
    private final MemberService memberService;

}
