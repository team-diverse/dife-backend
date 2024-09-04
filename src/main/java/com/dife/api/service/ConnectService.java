package com.dife.api.service;

import static java.util.stream.Collectors.toList;

import com.dife.api.exception.*;
import com.dife.api.model.*;
import com.dife.api.model.dto.ConnectPatchRequestDto;
import com.dife.api.model.dto.ConnectRequestDto;
import com.dife.api.model.dto.ConnectResponseDto;
import com.dife.api.repository.ConnectRepository;
import com.dife.api.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
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

		return modelMapper.map(connect, ConnectResponseDto.class);
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

		return modelMapper.map(connect, ConnectResponseDto.class);
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

	private void translateCreateConnect(
			String settingLanguageType, Member member, Member otherMember, Connect connect) {

		String message = "Hi!🤝 " + member.getUsername() + " wants to make connect with you!";

		switch (settingLanguageType) {
			case "EN":
				message = "Hi!🤝 " + member.getUsername() + " wants to make connect with you!";
				break;
			case "KO":
				message = "Hi!🤝 " + member.getUsername() + " 님이 회원님과 커넥트를 맺고 싶어 해요!";
				break;
			case "ZH":
				message = "Hi!🤝 " + member.getUsername() + " 想与您建立连接！";
				break;
			case "JA":
				message = "Hi!🤝 " + member.getUsername() + " があなたと接続したいと考えています！";
				break;
			case "ES":
				message = "Hi!🤝 " + member.getUsername() + " quiere conectarse contigo!";
				break;
		}

		notificationService.addNotifications(
				otherMember, member, message, NotificationType.CONNECT, connect.getId());
	}

	private void translateSuccessConnect(
			String settingLanguageType, Member member, Member otherMember, Long typeId) {
		String message = "YEAH!🙌 Succeed Connect With " + otherMember.getUsername() + "!";
		switch (settingLanguageType) {
			case "EN":
				message = "YEAH!🙌 Succeed Connect With " + otherMember.getUsername() + "!";
				break;
			case "KO":
				message = "YEAH!🙌 " + otherMember.getUsername() + " 님과의 커넥트가 성사되었어요!";
				break;
			case "ZH":
				message = "YEAH!🙌 " + otherMember.getUsername() + " 与您的连接成功建立！";
				break;
			case "JA":
				message = "YEAH!🙌 " + otherMember.getUsername() + " あなたとの接続が成功しました！";
				break;
			case "ES":
				message =
						"YEAH!🙌 " + otherMember.getUsername() + " ¡La conexión con usted ha sido exitosa!";
				break;
		}

		notificationService.addNotifications(
				member, otherMember, message, NotificationType.CONNECT, typeId);
	}
}
