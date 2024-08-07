package com.dife.api.service;

import com.dife.api.model.*;
import com.dife.api.model.dto.TranslationRequestDto;
import com.dife.api.model.dto.TranslationResponseDto;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TranslationService {

	private final String deeplApiUrl = "https://api-free.deepl.com/v2/translate";

	@Value("${spring.profiles.include}")
	private String authKey;

	public TranslationResponseDto translate(TranslationRequestDto requestDto) {
		String targetLang = requestDto.getTarget_lang();

		String apiUrl = deeplApiUrl + "?target_lang=" + targetLang;
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", "DeepL-Auth-Key " + authKey);

		TranslationResponseDto response =
				restTemplate.postForObject(
						apiUrl, createHttpEntity(requestDto, headers), TranslationResponseDto.class);

		return response;
	}

	private HttpEntity<TranslationRequestDto> createHttpEntity(
			TranslationRequestDto request, HttpHeaders headers) {
		return new HttpEntity<>(request, headers);
	}
}
