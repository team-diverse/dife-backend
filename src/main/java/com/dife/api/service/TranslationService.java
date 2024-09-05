package com.dife.api.service;

import com.dife.api.exception.*;
import com.dife.api.model.*;
import com.dife.api.model.dto.TranslationRequestDto;
import com.dife.api.model.dto.TranslationResponseDto;
import com.dife.api.repository.*;
import java.util.ArrayList;
import java.util.Arrays;
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
	private static final Integer RESTRICTED_TRANSLATION_COUNT = 200;
	private final BookmarkRepository bookmarkRepository;
	private final TranslationRepository translationRepository;
	private final PostRepository postRepository;
	private final CommentRepository commentRepository;
	private final MemberRepository memberRepository;
	private final ChatRepository chatRepository;

	@Value("${translation.api-key}")
	private String authKey;

	public TranslationResponseDto translate(TranslationRequestDto requestDto, String memberEmail) {

		log.info(String.valueOf(requestDto.getPostId()));
		Member member =
				memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);
		if (requestDto.getBookmarkId() != null) return translateBookmark(requestDto, member);
		if (requestDto.getPostId() != null) return translatePost(requestDto, member);
		if (requestDto.getCommentId() != null) return translateComment(requestDto, member);
		if (requestDto.getChatId() != null) return translateChat(requestDto, member);
		return translateBasic(requestDto);
	}

	public TranslationResponseDto translateBasic(TranslationRequestDto requestDto) {
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

	public TranslationResponseDto translateBookmark(TranslationRequestDto requestDto, Member member) {
		Bookmark bookmark =
				bookmarkRepository
						.findById(requestDto.getBookmarkId())
						.orElseThrow(BookmarkNotFoundException::new);

		if (member.getTranslationCount() > RESTRICTED_TRANSLATION_COUNT)
			throw new TranslationFullException();

		requestDto.setText(Collections.singletonList(bookmark.getMessage()));
		requestDto.setTarget_lang(member.getSettingLanguage());
		TranslationResponseDto response = translateBasic(requestDto);

		List<Translation> savedTranslations = new ArrayList<>();
		for (Translation translation : response.getTranslations()) {
			Translation savedTranslation = translationRepository.save(translation);
			savedTranslations.add(savedTranslation);
		}

		bookmark.setTranslations(savedTranslations);
		bookmarkRepository.save(bookmark);

		member.setTranslationCount(member.getTranslationCount() + 1);
		return response;
	}

	public TranslationResponseDto translatePost(TranslationRequestDto requestDto, Member member) {
		Post post =
				postRepository.findById(requestDto.getPostId()).orElseThrow(PostNotFoundException::new);

		if (member.getTranslationCount() > RESTRICTED_TRANSLATION_COUNT)
			throw new TranslationFullException();

		requestDto.setText(Arrays.asList(post.getTitle(), post.getContent()));
		requestDto.setTarget_lang(member.getSettingLanguage());
		return createTranslationResponse(requestDto, member);
	}

	public TranslationResponseDto translateComment(TranslationRequestDto requestDto, Member member) {
		Comment comment =
				commentRepository
						.findById(requestDto.getCommentId())
						.orElseThrow(CommentNotFoundException::new);

		if (member.getTranslationCount() > RESTRICTED_TRANSLATION_COUNT)
			throw new TranslationFullException();

		requestDto.setText(Collections.singletonList(comment.getContent()));
		requestDto.setTarget_lang(member.getSettingLanguage());
		return createTranslationResponse(requestDto, member);
	}

	public TranslationResponseDto translateChat(TranslationRequestDto requestDto, Member member) {
		Chat chat =
				chatRepository.findById(requestDto.getChatId()).orElseThrow(ChatNotFoundException::new);

		if (member.getTranslationCount() > RESTRICTED_TRANSLATION_COUNT)
			throw new TranslationFullException();

		requestDto.setText(Collections.singletonList(chat.getMessage()));
		requestDto.setTarget_lang(member.getSettingLanguage());
		return createTranslationResponse(requestDto, member);
	}

	private TranslationResponseDto createTranslationResponse(
			TranslationRequestDto requestDto, Member member) {
		TranslationResponseDto response = translateBasic(requestDto);

		List<Translation> savedTranslations = new ArrayList<>();

		for (Translation translation : response.getTranslations()) {
			Translation savedTranslation = translationRepository.save(translation);
			savedTranslations.add(savedTranslation);
		}

		member.setTranslationCount(member.getTranslationCount() + 1);
		response.setTranslations(savedTranslations);
		return response;
	}

	private HttpEntity<TranslationRequestDto> createHttpEntity(
			TranslationRequestDto request, HttpHeaders headers) {
		return new HttpEntity<>(request, headers);
	}
}
