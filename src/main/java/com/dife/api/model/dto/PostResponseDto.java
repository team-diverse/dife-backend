package com.dife.api.model.dto;

import com.dife.api.model.BoardCategory;
import com.dife.api.model.File;
import java.time.LocalDateTime;
import java.util.List;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDto {

	private Long id;

	private String title;

	private String content;

	private BoardCategory boardType;

	private Boolean isPublic = true;

	private Boolean isLiked = false;

	private Boolean isBookmarked = false;

	private Integer commentCount;

	private Integer likesCount;

	private Integer bookmarkCount;

	private LocalDateTime created;

	private LocalDateTime modified;

	private List<File> files;

	private MemberRestrictedResponseDto writer;
}
