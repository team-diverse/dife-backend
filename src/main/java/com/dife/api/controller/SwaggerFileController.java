package com.dife.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "File API", description = "파일 서비스 API")
public interface SwaggerFileController {

	@Operation(
			summary = "업로드된 이미지 파일 presignurl 조회 API",
			description =
					"조회하고자 하는 이미지 파일의 이름(OriginalName - ex. cookie.jpeg) 을 입력해 이미지 파일의 presignUrl을 확인할 수 있는 API입니다. 존재하지 않는 파일을 업로드 해도 presignUrl이 나오지만 No Such element라는 xml이 표시될 것입니다.")
	@ApiResponse(responseCode = "200")
	ResponseEntity<String> getFile(@RequestParam(name = "fileName") String fileName)
			throws IOException;
}
