package com.diverse.dife.service.schoolInfo;


import com.diverse.dife.repository.schoolInfo.SchoolMealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SchoolMealService {

    private final SchoolMealRepository schoolMealRepository;
}
