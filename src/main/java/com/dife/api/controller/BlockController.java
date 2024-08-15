package com.dife.api.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.dife.api.model.dto.BlockMemberRequestDto;
import com.dife.api.model.dto.BlockMemberResponseDto;
import com.dife.api.service.BlockService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/blocks")
public class BlockController implements SwaggerBlockController {

	private final BlockService blockService;

	@PostMapping
	public ResponseEntity<List<BlockMemberResponseDto>> createBlock(
			@RequestBody BlockMemberRequestDto requestDto, Authentication auth) {

		List<BlockMemberResponseDto> responseDto =
				blockService.createBlackList(requestDto, auth.getName());
		return ResponseEntity.status(CREATED).body(responseDto);
	}

	@GetMapping
	public ResponseEntity<List<BlockMemberResponseDto>> createBlock(Authentication auth) {
		List<BlockMemberResponseDto> responseDto = blockService.getBlackList(auth.getName());
		return ResponseEntity.status(OK).body(responseDto);
	}

	@DeleteMapping
	public ResponseEntity<Void> deleteBlock(
			@RequestParam(name = "memberId") Long memberId, Authentication auth) {
		blockService.deleteBlock(memberId, auth.getName());
		return new ResponseEntity<>(OK);
	}
}
