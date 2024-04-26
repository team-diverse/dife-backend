package com.dife.api.model.dto;

import com.dife.api.model.ConnectStatus;
import com.dife.api.model.Member;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ConnectResponseDto {
    private Long id;

    @NotNull
    @JsonProperty("from_member")
    private Member fromMember;

    @NotNull
    @JsonProperty("to_member")
    private Member toMember;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ConnectStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
