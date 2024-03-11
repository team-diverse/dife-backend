package com.diverse.dife.service.matching;

import com.diverse.dife.repository.MatchingRespository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OneToOneMatchingService {

    private final MatchingRespository matchingRespository;
}
