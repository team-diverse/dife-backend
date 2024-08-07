package com.dife.api.service;

import com.dife.api.exception.BookmarkNotFoundException;
import com.dife.api.model.*;
import com.dife.api.model.dto.TranslationRequestDto;
import com.dife.api.model.dto.TranslationResponseDto;
import com.dife.api.repository.BookmarkRepository;
import com.dife.api.repository.TranslationRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
	private final BookmarkRepository bookmarkRepository;
	private final TranslationRepository translationRepository;

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

		if (requestDto.getBookmarkId() != null) {
			Bookmark bookmark =
					bookmarkRepository
							.findById(requestDto.getBookmarkId())
							.orElseThrow(BookmarkNotFoundException::new);
			assert response != null;
			List<Translation> savedTranslations = new ArrayList<>();
			for (Translation translation : response.getTranslations()) {
				Translation savedTranslation = translationRepository.save(translation);
				savedTranslations.add(savedTranslation);
			}
			bookmark.setTranslations(savedTranslations);
			bookmarkRepository.save(bookmark);
		}
		return response;
	}

	private HttpEntity<TranslationRequestDto> createHttpEntity(
			TranslationRequestDto request, HttpHeaders headers) {
		return new HttpEntity<>(request, headers);
	}
}
