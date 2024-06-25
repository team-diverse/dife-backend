package com.dife.api.model.dto;

import com.dife.api.model.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "채팅방 응답 객체")
public class ChatroomResponseDto {

	private Long id;

	private String name;

	private ChatroomType chatroomType;

	private String description;

	private File profileImg;

	@Schema(description = "Presigned S3경로")
	private String profilePresignUrl;

	private Set<String> hobbies;

	private Integer count;

	private Integer maxCount;

	private Set<String> languages;

	private Set<String> purposes;

	private Boolean isPublic;

	private String password;

	private LocalDateTime created;

	private Set<Member> members;

	private Set<Chat> chats;
}
