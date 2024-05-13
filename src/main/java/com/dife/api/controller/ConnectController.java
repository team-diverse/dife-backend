package com.dife.api.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.dife.api.model.dto.ConnectPatchRequestDto;
import com.dife.api.model.dto.ConnectRequestDto;
import com.dife.api.model.dto.ConnectResponseDto;
import com.dife.api.service.ConnectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Connect API", description = "Connect API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/connects")
public class ConnectController {

	private final ConnectService connectService;

	@GetMapping("/")
	@Operation(summary = "커넥트 목록 조회 API", description = "회원이 맺은 커넥트 목록을 조회합니다.")
	public ResponseEntity<List<ConnectResponseDto>> getConnects(Authentication auth) {
		List<ConnectResponseDto> responseDto = connectService.getConnects(auth.getName());
		return ResponseEntity.status(OK).body(responseDto);
	}

	@GetMapping(value = "/", params = "member_id")
	@Operation(summary = "특정 커넥트 조회 API", description = "회원과 특정 회원과의 커넥트를 조회합니다.")
	@ApiResponse(
			responseCode = "200",
			description = "커넥트 조회 성공 예시",
			content = {
				@Content(
						mediaType = "application/json",
						schema = @Schema(implementation = ConnectResponseDto.class))
			})
	public ResponseEntity<ConnectResponseDto> getConnect(
			@RequestParam(name = "member_id") Long memberId, Authentication auth) {
		ConnectResponseDto responseDto = connectService.getConnect(memberId, auth.getName());
		return ResponseEntity.status(OK).body(responseDto);
	}

	@PostMapping(value = "/", consumes = "application/json")
	@Operation(summary = "커넥트 요청 API", description = "커넥트 요청을 생성합니다.")
	@ApiResponse(
			responseCode = "201",
			description = "커넥트 요청 성공 예시",
			content = {
				@Content(
						mediaType = "application/json",
						schema = @Schema(implementation = ConnectResponseDto.class))
			})
	public ResponseEntity<ConnectResponseDto> createConnect(
			@Valid @RequestBody ConnectRequestDto requestDto, Authentication auth) {
		ConnectResponseDto responseDto = connectService.saveConnect(requestDto, auth.getName());
		return ResponseEntity.status(CREATED).body(responseDto);
	}

	@PatchMapping(value = "/", consumes = "application/json")
	@Operation(summary = "커넥트 수락 API", description = "커넥트 요청 수락 여부를 결정합니다. ")
	public ResponseEntity<Void> acceptConnect(
			@Valid @RequestBody ConnectPatchRequestDto requestDto, Authentication auth) {
		connectService.acceptConnect(requestDto, auth.getName());
		return new ResponseEntity<>(OK);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "커넥트 삭제 API", description = "커넥트를 맺을 회원 Id를 입력합니다.")
	@ApiResponse(responseCode = "200", description = "커넥트 삭제 성공 예시")
	public ResponseEntity<Void> deleteConnect(
			@PathVariable(name = "id") Long id, Authentication auth) {
		connectService.deleteConnect(id, auth.getName());
		return new ResponseEntity<>(OK);
	}
}
