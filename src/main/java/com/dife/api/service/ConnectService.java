package com.dife.api.service;

import static java.util.stream.Collectors.toList;

import com.dife.api.exception.*;
import com.dife.api.model.*;
import com.dife.api.model.dto.ConnectPatchRequestDto;
import com.dife.api.model.dto.ConnectRequestDto;
import com.dife.api.model.dto.ConnectResponseDto;
import com.dife.api.model.dto.MemberRestrictedResponseDto;
import com.dife.api.repository.ConnectRepository;
import com.dife.api.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ConnectService {
	private final ConnectRepository connectRepository;
	private final MemberRepository memberRepository;

	private final ModelMapper modelMapper;

	private final NotificationService notificationService;

	@Transactional(readOnly = true)
	public List<ConnectResponseDto> getConnects(ConnectStatus status, String currentMemberEmail) {

		List<Connect> allConnects = new ArrayList<>();
		Member currentMember =
				memberRepository.findByEmail(currentMemberEmail).orElseThrow(MemberNotFoundException::new);

		switch (status) {
			case ACCEPTED:
				allConnects =
						connectRepository.findAllByMemberAndStatus(currentMember, ConnectStatus.ACCEPTED);
				break;
			case PENDING:
				allConnects =
						connectRepository.findAll().stream()
								.filter(connect -> connect.getStatus().equals(ConnectStatus.PENDING))
								.filter(
										connect ->
												connect.getToMember().getId().equals(currentMember.getId())
														|| connect.getFromMember().getId().equals(currentMember.getId()))
								.collect(toList());
				break;
		}

		return allConnects.stream()
				.map(c -> modelMapper.map(c, ConnectResponseDto.class))
				.collect(toList());
	}

	@Transactional(readOnly = true)
	public ConnectResponseDto getConnect(Long memberId, String currentMemberEmail) {
		Member currentMember =
				memberRepository.findByEmail(currentMemberEmail).orElseThrow(MemberNotFoundException::new);
		Member otherMember =
				memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);

		List<Connect> connections = connectRepository.findByMemberPair(currentMember, otherMember);

		Connect connect = connections.stream().findFirst().orElseThrow(ConnectNotFoundException::new);

		ConnectResponseDto responseDto = modelMapper.map(connect, ConnectResponseDto.class);
		responseDto.setToMember(modelMapper.map(otherMember, MemberRestrictedResponseDto.class));
		responseDto.setFromMember(modelMapper.map(currentMember, MemberRestrictedResponseDto.class));
		return responseDto;
	}

	public ConnectResponseDto saveConnect(ConnectRequestDto dto, String currentMemberEmail) {
		Member currentMember =
				memberRepository.findByEmail(currentMemberEmail).orElseThrow(MemberNotFoundException::new);
		Member toMember =
				memberRepository.findById(dto.getToMemberId()).orElseThrow(MemberNotFoundException::new);

		if (currentMember.getId().equals(toMember.getId())) {
			throw new IdenticalConnectException();
		}
		List<Connect> existingConnections = connectRepository.findByMemberPair(currentMember, toMember);

		if (!existingConnections.isEmpty()) {
			throw new ConnectDuplicateException();
		}

		Connect connect = new Connect();
		connect.setFromMember(currentMember);
		connect.setToMember(toMember);
		connect.setStatus(ConnectStatus.PENDING);
		connectRepository.save(connect);

		translateCreateConnect(toMember.getSettingLanguage(), currentMember, toMember, connect);

		ConnectResponseDto responseDto = modelMapper.map(connect, ConnectResponseDto.class);
		responseDto.setToMember(modelMapper.map(toMember, MemberRestrictedResponseDto.class));
		responseDto.setFromMember(modelMapper.map(currentMember, MemberRestrictedResponseDto.class));
		return responseDto;
	}

	public void acceptConnect(ConnectPatchRequestDto requestDto, String currentMemberEmail) {
		Member currentMember =
				memberRepository.findByEmail(currentMemberEmail).orElseThrow(MemberNotFoundException::new);
		Member otherMember =
				memberRepository
						.findById(requestDto.getMemberId())
						.orElseThrow(MemberNotFoundException::new);

		Connect connect =
				connectRepository
						.findByFromMemberAndToMember(otherMember, currentMember)
						.orElseThrow(ConnectNotFoundException::new);
		connect.setStatus(ConnectStatus.ACCEPTED);

		createNotifications(currentMember, otherMember.getEmail(), connect.getId());

		createNotifications(otherMember, currentMember.getEmail(), connect.getId());
	}

	public void deleteConnect(Long id, String email) {
		if (!isConnectRelevant(id, email)) {
			throw new ConnectUnauthorizedException();
		}
		connectRepository.deleteById(id);
	}

	public boolean isConnectRelevant(Long connectId, String email) {
		Connect connect =
				connectRepository.findById(connectId).orElseThrow(ConnectNotFoundException::new);
		String fromMemberEmail = connect.getFromMember().getEmail();
		String toMemberEmail = connect.getToMember().getEmail();
		return fromMemberEmail.equals(email) || toMemberEmail.equals(email);
	}

	public boolean isConnected(Member member1, Member member2) {
		List<Connect> connections = connectRepository.findByMemberPair(member1, member2);

		return connections.stream()
				.anyMatch(connect -> connect.getStatus().equals(ConnectStatus.ACCEPTED));
	}

	public boolean hasPendingConnect(Member fromMember, Member toMember) {
		return connectRepository
				.findByFromMemberAndToMember(fromMember, toMember)
				.map(connect -> connect.getStatus().equals(ConnectStatus.PENDING))
				.orElse(false);
	}

	private void createNotifications(Member member, String otherMemberEmail, Long typeId) {

		Member otherMember =
				memberRepository.findByEmail(otherMemberEmail).orElseThrow(MemberNotFoundException::new);
		translateSuccessConnect(member.getSettingLanguage(), member, otherMember, typeId);
	}

	private String translationDivide(String settingLanguage, Boolean isSuccess) {
		ResourceBundle resourceBundle;
		if (isSuccess) {
			resourceBundle = ResourceBundle.getBundle("notification.successConnect", Locale.getDefault());
		} else {
			resourceBundle = ResourceBundle.getBundle("notification.createConnect", Locale.getDefault());
		}

		return resourceBundle.getString(settingLanguage.toUpperCase());
	}

	private void translateCreateConnect(
			String settingLanguageType, Member member, Member otherMember, Connect connect) {

		String message = "Hi!🤝 " + member.getUsername() + " ";

		message += translationDivide(settingLanguageType, false);

		notificationService.addNotifications(
				otherMember, member, message, NotificationType.CONNECT, connect.getId());
	}

	private void translateSuccessConnect(
			String settingLanguageType, Member member, Member otherMember, Long typeId) {
		String message = "YEAH!🙌 With " + otherMember.getUsername() + " ";

		message += translationDivide(settingLanguageType, true);

		notificationService.addNotifications(
				member, otherMember, message, NotificationType.CONNECT, typeId);
	}
}
