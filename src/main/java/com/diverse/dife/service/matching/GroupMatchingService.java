package com.diverse.dife.service.matching;

import com.diverse.dife.repository.MatchingRespository;
import com.diverse.dife.repository.schoolInfo.BuildingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class GroupMatchingService {

    private final MatchingRespository matchingRespository;
}
