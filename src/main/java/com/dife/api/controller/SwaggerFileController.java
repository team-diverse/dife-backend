package com.dife.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "File API", description = "파일 서비스 API")
public interface SwaggerFileController {

	@Operation(
			summary = "업로드된 이미지 파일 presignurl 조회 API",
			description =
					"조회하고자 하는 이미지 파일의 고유 Id로 URL GET요청을 통해 이미지 파일의 presignUrl을 확인할 수 있는 API입니다. 존재하지 않는 파일을 업로드 해도 presignUrl이 나오지만 No Such element라는 xml이 표시될 것입니다.")
	@ApiResponse(responseCode = "200")
	ResponseEntity<String> getFile(@PathVariable("id") Long id, Authentication auth);
}
