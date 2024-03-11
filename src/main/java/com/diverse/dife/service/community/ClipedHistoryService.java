package com.diverse.dife.service.community;

import com.diverse.dife.repository.community.ClipedHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ClipedHistoryService {

    private final ClipedHistoryRepository clipedHistoryRepository;

}
