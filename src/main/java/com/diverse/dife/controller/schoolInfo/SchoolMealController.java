package com.diverse.dife.controller.schoolInfo;

import com.diverse.dife.service.TranslationService;
import com.diverse.dife.service.schoolInfo.SchoolMealService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schoolmeal")
@Slf4j
public class SchoolMealController {

    private final SchoolMealService schoolMealService;
    private final TranslationService translationService;

}
