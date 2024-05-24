package com.dife.api.config;

import com.dife.api.model.Chatroom;
import com.dife.api.model.GroupPurpose;
import com.dife.api.model.Language;
import com.dife.api.model.Tag;
import com.dife.api.model.dto.ChatroomResponseDto;
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
				.typeMap(Tag.class, String.class)
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
									src -> src.getChatroom_setting().getProfileImgName(),
									ChatroomResponseDto::setProfileImgName);
							mapper.map(
									src -> src.getChatroom_setting().getDescription(),
									ChatroomResponseDto::setDescription);
							mapper.map(
									src -> src.getChatroom_setting().getCount(), ChatroomResponseDto::setCount);
							mapper.map(
									src -> src.getChatroom_setting().getMaxCount(), ChatroomResponseDto::setMaxCount);
							mapper.map(
									src -> src.getChatroom_setting().getIsPublic(), ChatroomResponseDto::setIsPublic);
							mapper.map(
									src ->
											Optional.ofNullable(src.getChatroom_setting().getTags())
													.orElse(Collections.emptySet())
													.stream()
													.map(Tag::getName)
													.collect(Collectors.toSet()),
									ChatroomResponseDto::setTags);

							mapper.map(
									src ->
											Optional.ofNullable(src.getChatroom_setting().getLanguages())
													.orElse(Collections.emptySet())
													.stream()
													.map(Language::getName)
													.collect(Collectors.toSet()),
									ChatroomResponseDto::setLanguages);

							mapper.map(
									src ->
											Optional.ofNullable(src.getChatroom_setting().getPurposes())
													.orElse(Collections.emptySet())
													.stream()
													.map(GroupPurpose::getName)
													.collect(Collectors.toSet()),
									ChatroomResponseDto::setPurposes);
							mapper.map(
									src -> src.getChatroom_setting().getPassword(), ChatroomResponseDto::setPassword);
						});

		return modelMapper;
	}
}
