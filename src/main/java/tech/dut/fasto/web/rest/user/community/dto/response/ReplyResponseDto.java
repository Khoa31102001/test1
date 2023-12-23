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
public class ReplyResponseDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -1597269799178670753L;

    private Long id;
    private Long reviewId;
    private String content;
    private Long reReplyId;
    
    private Long userId;

    private Long amountLike;

    @JsonSerialize(using = InstantSerializerCustomizer.class)
    private Instant modifiedAt;

    public ReplyResponseDto(Long id, Long reviewId, String content, Long userId , Long amountLike, Instant modifiedAt ) {
        this.id = id;
        this.reviewId = reviewId;
        this.content = content;
        this.userId = userId;
        this.amountLike = amountLike;
        this.modifiedAt = modifiedAt;
    }
}
