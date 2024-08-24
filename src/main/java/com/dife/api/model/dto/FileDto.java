package com.dife.api.model.dto;

import com.dife.api.model.Format;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileDto {
	private Long id;

	private String originalName;

	private String name;

	private String size;

	@Enumerated(EnumType.STRING)
	private Format format;

	private LocalDateTime createdAt;
}
