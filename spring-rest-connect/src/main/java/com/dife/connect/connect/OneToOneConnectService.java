package com.dife.connect.connect;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OneToOneConnectService {

    private final ConnectRepository connectRepository;
}
