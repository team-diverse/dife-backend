package com.dife.api.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class ChatRoomSetting {

    private String chatRoomBio;
    private Integer min_count;
    private Integer max_count;

}
