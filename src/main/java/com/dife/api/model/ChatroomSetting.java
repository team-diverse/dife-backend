package com.dife.api.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class ChatroomSetting {

    private String description;
    private Integer min_count;
    private Integer max_count;

}
