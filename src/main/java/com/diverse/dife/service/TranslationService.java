package com.diverse.dife.service;

import com.diverse.dife.repository.TranslationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TranslationService {

    private final TranslationRepository translationRepository;
}
