package tech.dut.fasto.web.rest.user.community.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDto {
    private Long shopId;

    @NotBlank(message = "validate.not.empty")
    private String content;

    @NotBlank(message = "validate.not.empty")
    private String title;

    private List<String> reviewImages;
}