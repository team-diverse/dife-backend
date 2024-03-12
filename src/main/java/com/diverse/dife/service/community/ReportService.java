package com.diverse.dife.service.community;

import com.diverse.dife.repository.community.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
// 신고 서비스
public class ReportService {

    private final ReportRepository reportRepository;
}
