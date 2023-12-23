package tech.dut.fasto.web.rest.user.community.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.dut.fasto.config.jackson.InstantSerializerCustomizer;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class ReviewResponseDto implements Serializable {


    @Serial
    private static final long serialVersionUID = 3815799699720256096L;
    private Long id;
    private String title;
    private String content;
    private Long shopId;
    private Long userId;
    private Long amountLike;
    
    private Boolean isFavorite;

    @JsonSerialize(using = InstantSerializerCustomizer.class)
    private Instant modifiedAt;

    public ReviewResponseDto(Long id, String title, String content, Long shopId, Long userId, Long amountLike, Instant modifiedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.shopId = shopId;
        this.userId = userId;
        this.amountLike = amountLike;
        this.modifiedAt = modifiedAt;
    }
}
