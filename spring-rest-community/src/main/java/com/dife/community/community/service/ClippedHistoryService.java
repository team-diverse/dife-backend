package com.dife.community.community.service;

import com.dife.community.community.repository.ClippedHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ClippedHistoryService {

    private final ClippedHistoryRepository clippedHistoryRepository;
}
