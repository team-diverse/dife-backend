package com.dife.api.config;

import com.dife.api.model.*;
import com.dife.api.model.dto.ChatroomResponseDto;
import com.dife.api.model.dto.MemberResponseDto;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean(name = "chatroomModelMapper")
	public ModelMapper chatroomModelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		modelMapper
				.typeMap(Hobby.class, String.class)
				.setConverter(context -> context.getSource().getName());
		modelMapper
				.typeMap(Language.class, String.class)
				.setConverter(context -> context.getSource().getName());
		modelMapper
				.typeMap(GroupPurpose.class, String.class)
				.setConverter(context -> context.getSource().getName());

		modelMapper
				.typeMap(Chatroom.class, ChatroomResponseDto.class)
				.addMappings(
						mapper -> {
							mapper.map(
									src -> src.getChatroomSetting().getProfileImg(),
									ChatroomResponseDto::setProfileImg);
							mapper.map(
									src -> src.getChatroomSetting().getDescription(),
									ChatroomResponseDto::setDescription);
							mapper.map(src -> src.getChatroomSetting().getCount(), ChatroomResponseDto::setCount);
							mapper.map(
									src -> src.getChatroomSetting().getMaxCount(), ChatroomResponseDto::setMaxCount);
							mapper.map(
									src -> src.getChatroomSetting().getIsPublic(), ChatroomResponseDto::setIsPublic);
							mapper.map(
									src ->
											Optional.ofNullable(src.getChatroomSetting().getHobbies())
													.orElse(Collections.emptySet())
													.stream()
													.map(Hobby::getName)
													.collect(Collectors.toSet()),
									ChatroomResponseDto::setHobbies);

							mapper.map(
									src ->
											Optional.ofNullable(src.getChatroomSetting().getLanguages())
													.orElse(Collections.emptySet())
													.stream()
													.map(Language::getName)
													.collect(Collectors.toSet()),
									ChatroomResponseDto::setLanguages);

							mapper.map(
									src ->
											Optional.ofNullable(src.getChatroomSetting().getPurposes())
													.orElse(Collections.emptySet())
													.stream()
													.map(GroupPurpose::getName)
													.collect(Collectors.toSet()),
									ChatroomResponseDto::setPurposes);
							mapper.map(
									src -> src.getChatroomSetting().getPassword(), ChatroomResponseDto::setPassword);
						});

		return modelMapper;
	}

	@Bean(name = "memberModelMapper")
	public ModelMapper memberModelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		modelMapper
				.typeMap(Hobby.class, String.class)
				.setConverter(context -> context.getSource().getName());
		modelMapper
				.typeMap(Language.class, String.class)
				.setConverter(context -> context.getSource().getName());
		modelMapper
				.typeMap(Member.class, MemberResponseDto.class)
				.addMappings(
						mapper -> {
							mapper.map(Member::getUsername, MemberResponseDto::setUsername);
							mapper.map(Member::getIsKorean, MemberResponseDto::setIsKorean);
							mapper.map(Member::getBio, MemberResponseDto::setBio);
							mapper.map(Member::getMbti, MemberResponseDto::setMbti);
							mapper.map(Member::getIsPublic, MemberResponseDto::setIsPublic);
							mapper.map(Member::getProfileImg, MemberResponseDto::setProfileImg);
							mapper.map(Member::getIsVerified, MemberResponseDto::setIsVerified);

							mapper.map(
									src ->
											Optional.ofNullable(src.getHobbies()).orElse(Collections.emptySet()).stream()
													.map(Hobby::getName)
													.collect(Collectors.toSet()),
									MemberResponseDto::setHobbies);

							mapper.map(
									src ->
											Optional.ofNullable(src.getLanguages())
													.orElse(Collections.emptySet())
													.stream()
													.map(Language::getName)
													.collect(Collectors.toSet()),
									MemberResponseDto::setLanguages);
						});

		return modelMapper;
	}
}
