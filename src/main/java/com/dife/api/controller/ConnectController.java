package com.dife.api.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.dife.api.model.ConnectStatus;
import com.dife.api.model.dto.ConnectPatchRequestDto;
import com.dife.api.model.dto.ConnectRequestDto;
import com.dife.api.model.dto.ConnectResponseDto;
import com.dife.api.service.ConnectService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/connects")
public class ConnectController implements SwaggerConnectController {

	private final ConnectService connectService;

	@GetMapping
	public ResponseEntity<List<ConnectResponseDto>> getConnects(
			@RequestParam(name = "status", required = false) ConnectStatus status, Authentication auth) {
		List<ConnectResponseDto> responseDto = connectService.getConnects(status, auth.getName());
		return ResponseEntity.status(OK).body(responseDto);
	}

	@GetMapping("/")
	public ResponseEntity<ConnectResponseDto> getConnect(
			@RequestParam(name = "member_id") Long memberId, Authentication auth) {
		ConnectResponseDto responseDto = connectService.getConnect(memberId, auth.getName());
		return ResponseEntity.status(OK).body(responseDto);
	}

	@PostMapping("/")
	public ResponseEntity<ConnectResponseDto> createConnect(
			@Valid @RequestBody ConnectRequestDto requestDto, Authentication auth) {
		ConnectResponseDto responseDto = connectService.saveConnect(requestDto, auth.getName());
		return ResponseEntity.status(CREATED).body(responseDto);
	}

	@PatchMapping("/")
	public ResponseEntity<Void> acceptConnect(
			@Valid @RequestBody ConnectPatchRequestDto requestDto, Authentication auth) {
		connectService.acceptConnect(requestDto, auth.getName());
		return new ResponseEntity<>(OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteConnect(
			@PathVariable(name = "id") Long id, Authentication auth) {
		connectService.deleteConnect(id, auth.getName());
		return new ResponseEntity<>(OK);
	}
}
