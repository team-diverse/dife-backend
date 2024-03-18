package com.dife.connect.connect;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/onetooneconnect")
@Slf4j
public class SingleConnectController {

    private final SingleConnectService singleConnectService;
}
