package com.diverse.dife.controller.schoolInfo;


import com.diverse.dife.service.TranslationService;
import com.diverse.dife.service.community.CommentService;
import com.diverse.dife.service.schoolInfo.BuildingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/building")
public class BuildingController {

    private final BuildingService buildingService;
    private final TranslationService translationService;
}
