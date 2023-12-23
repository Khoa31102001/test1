package tech.dut.fasto.web.rest.user.community.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.dut.fasto.config.jackson.InstantSerializerCustomizer;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReReplyResponseDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -3763169309381322793L;
    private Long id;
    private Long reviewId;
    private String content;
    private Long replyId;
    private Long userId;
    private Long amountLike;
    @JsonSerialize(using = InstantSerializerCustomizer.class)
    private Instant modifiedAt;
    
}
