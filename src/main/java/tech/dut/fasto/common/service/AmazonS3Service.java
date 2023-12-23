package tech.dut.fasto.common.service;

import org.springframework.web.multipart.MultipartFile;
import tech.dut.fasto.common.dto.request.ImageUploadDto;
import tech.dut.fasto.common.dto.response.PreSignedURL;

import java.io.File;
import java.util.List;

public interface AmazonS3Service {
    PreSignedURL uploadPreSignedUrl(String fileName);

    String uploadFileTos3bucket(String fileName, File file);

    String uploadFile(MultipartFile multipartFile, String folderType);

    List<PreSignedURL> uploadImagesWithPreSigned(List<ImageUploadDto> imageUploadDtos);

    List<String> uploadFiles(List<MultipartFile> multipartFiles, String folderType);
}