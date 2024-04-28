package com.dife.api.service;

import com.dife.api.exception.ChatRoomCountException;
import com.dife.api.exception.ChatRoomDuplicateException;
import com.dife.api.exception.ChatRoomException;
import com.dife.api.model.*;
import com.dife.api.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ChatRoomService {

    @Autowired
    private final ChatRoomRepository chatRoomRepository;

    public ChatRoom createGroupChatRoom(String chatRoomName, String chatRoomBio, String max_count, String min_count)
    {
        ChatRoom chatRoom = new ChatRoom();
        if (chatRoomRepository.existsByChatRoomName(chatRoomName))
        {
            throw new ChatRoomDuplicateException();
        }
        if (chatRoomName == null || chatRoomName.isEmpty())
        {
            throw new ChatRoomException("채팅방 이름은 필수사항입니다.");
        }
        chatRoom.setChatRoomName(chatRoomName);
        chatRoom.setChatRoomType(ChatRoomType.GROUP);

        ChatRoomSetting chatRoomSetting = new ChatRoomSetting();

        if (min_count == null || max_count == null || min_count.isEmpty() || max_count.isEmpty())
        {
            throw new ChatRoomException("채팅방 인원 설정은 필수사항입니다.");
        }

        int maxCount = Integer.parseInt(max_count);
        int minCount = Integer.parseInt(min_count);
        if (maxCount > 30 || minCount < 3)
        {
            throw new ChatRoomCountException();
        }
        chatRoomSetting.setMax_count(maxCount);
        chatRoomSetting.setMin_count(minCount);

        if (chatRoomBio == null || chatRoomBio.isEmpty())
        {
            throw new ChatRoomException("채팅방 이름은 필수사항입니다.");
        }
        chatRoomSetting.setChatRoomBio(chatRoomBio);

        chatRoom.setChatRoomSetting(chatRoomSetting);

        chatRoomRepository.save(chatRoom);

        return chatRoom;
    }
}
