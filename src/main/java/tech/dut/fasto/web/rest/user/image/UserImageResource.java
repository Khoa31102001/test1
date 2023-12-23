package tech.dut.fasto.web.rest.user.image;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.dut.fasto.common.domain.enumeration.ImageFolderEnum;
import tech.dut.fasto.common.dto.request.ImageUploadDto;
import tech.dut.fasto.common.dto.response.PreSignedURL;
import tech.dut.fasto.common.service.AmazonS3Service;

import java.util.List;

@RestController
@RequestMapping("/user/management")
@RequiredArgsConstructor
@Validated
public class UserImageResource {
    private final AmazonS3Service amazonS3Service;

    @ApiOperation(value = "Get pre-signed URL for upload images to s3")
    @PostMapping("/images/pre-signed")
    public ResponseEntity<List<PreSignedURL>> uploadImages(
            @ApiParam(value = "List fileName", required = true) @RequestBody List<ImageUploadDto> images)  {
        return ResponseEntity.ok(this.amazonS3Service.uploadImagesWithPreSigned(images));
    }

    @ApiOperation(value = "Upload image with s3")
    @PostMapping("/images/upload")
    public ResponseEntity<List<String>> uploadFiles(
            @ApiParam(value = "List file") @RequestBody List<MultipartFile> files, @ApiParam("FolderType") @RequestParam ImageFolderEnum imageFolderType)  {
        return ResponseEntity.ok(this.amazonS3Service.uploadFiles(files, imageFolderType.toString()));
    }
}
