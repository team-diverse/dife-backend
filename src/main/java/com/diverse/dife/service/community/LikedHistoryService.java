package com.diverse.dife.service.community;

import com.diverse.dife.repository.community.LikedHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LikedHistoryService {

    private final LikedHistoryRepository likedHistoryRepository;

}
