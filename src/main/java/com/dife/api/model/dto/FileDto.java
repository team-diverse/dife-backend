package com.dife.api.model.dto;

import com.dife.api.model.Format;
import jakarta.validation.constraints.NotNull;
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

	@NotNull private String name;

	@NotNull private String size;

	@NotNull private String url;

	@NotNull private Format format;

	private LocalDateTime createdAt;
}
