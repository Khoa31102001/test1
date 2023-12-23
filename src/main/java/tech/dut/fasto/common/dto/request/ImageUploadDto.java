package tech.dut.fasto.common.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import tech.dut.fasto.common.domain.enumeration.ImageFolderEnum;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ImageUploadDto {
    @NotEmpty(message = "File name is required")
    @NotNull(message = "File name is required")
    @ApiModelProperty(notes = "File name", required = true)
    private String fileName;

    @NotEmpty(message = "File type is required")
    @NotNull(message = "File type is required")
    @ApiModelProperty(notes = "File type", required = true)
    private String fileType;

    @NotEmpty(message = "Image folder type is not null")
    @ApiModelProperty(notes = "Image folder type", required = true)
    private ImageFolderEnum imageFolderType;
}
