package dto;

import com.dife.member.model.MBTI_category;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @NotNull
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    private String password;

    @NotNull
    private Boolean is_korean;

    @NotNull
    private String bio;

    private String file_id;

    @Enumerated(EnumType.STRING)
    private MBTI_category mbti;

    @NotNull
    private Boolean is_public;

    private String nickname;
}
