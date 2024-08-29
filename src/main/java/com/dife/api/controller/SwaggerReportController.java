package com.dife.api.controller;

import com.dife.api.model.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Report API", description = "신고 API")
public interface SwaggerReportController {

	@Operation(
			summary = "신고 생성 API",
			description =
					"사용자가 신고 내용인(type)을 작성하고 신고하고자 하는 게시글/댓글/사용자/채팅방 ID를 작성한 DTO를 생성해 신고 서비스를 진행하는 API입니다.")
	ResponseEntity<Void> createDeclaration(
			@RequestBody ReportRequestDto requestDto, Authentication auth) throws IOException;
}
