package com.dife.api.config;

import com.dife.api.model.*;
import com.dife.api.model.dto.GroupChatroomResponseDto;
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
				.setConverter(context -> context.getSource().getType().name());
		modelMapper
				.typeMap(Chatroom.class, GroupChatroomResponseDto.class)
				.addMappings(
						mapper -> {
							mapper.map(
									src ->
											Optional.ofNullable(src.getChatroomSetting().getHobbies())
													.orElse(Collections.emptySet())
													.stream()
													.map(Hobby::getName)
													.collect(Collectors.toSet()),
									GroupChatroomResponseDto::setHobbies);

							mapper.map(
									src ->
											Optional.ofNullable(src.getChatroomSetting().getLanguages())
													.orElse(Collections.emptySet())
													.stream()
													.map(Language::getName)
													.collect(Collectors.toSet()),
									GroupChatroomResponseDto::setLanguages);
							mapper.map(
									src ->
											Optional.ofNullable(src.getChatroomSetting().getPurposes())
													.orElse(Collections.emptySet())
													.stream()
													.map(GroupPurpose::getType)
													.collect(Collectors.toSet()),
									GroupChatroomResponseDto::setPurposes);
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
							mapper.map(Member::getCreated, MemberResponseDto::setCreated);
							mapper.map(Member::getModified, MemberResponseDto::setModified);
							mapper.map(Member::getUsername, MemberResponseDto::setUsername);
							mapper.map(Member::getCountry, MemberResponseDto::setCountry);
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
