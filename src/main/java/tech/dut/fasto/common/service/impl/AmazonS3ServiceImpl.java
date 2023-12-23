package tech.dut.fasto.common.service.impl;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tech.dut.fasto.common.dto.request.ImageUploadDto;
import tech.dut.fasto.common.dto.response.PreSignedURL;
import tech.dut.fasto.common.service.AmazonS3Service;

import tech.dut.fasto.config.properties.FastoProperties;


import tech.dut.fasto.errors.FastoAlertException;
import tech.dut.fasto.util.constants.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AmazonS3ServiceImpl implements AmazonS3Service {
    private final FastoProperties fastoProperties;
    private final AmazonS3 s3client;

    private final MessageService messageService;

    @Override
    public PreSignedURL uploadPreSignedUrl(String fileName) {
        PreSignedURL preSignedAWS = new PreSignedURL();
        Date date = new Date();
        date = DateUtils.addMinutes(date, this.fastoProperties.getS3().getAws().getPreSignedExpired());
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(this.fastoProperties.getS3().getAws().getBucketName(), fileName);
        generatePresignedUrlRequest.setMethod(HttpMethod.PUT);
        generatePresignedUrlRequest.setExpiration(date);
        URL url = this.s3client.generatePresignedUrl(generatePresignedUrlRequest);
        preSignedAWS.setPreSignedURL(url);
        String urlRes = url.getProtocol() + "://" + fastoProperties.getS3().getAws().getBucketName() + Constants.BREAK_POINT + Constants.S3 + Constants.BREAK_POINT + fastoProperties.getS3().getAws().getRegion() + Constants.BREAK_POINT + Constants.URL_AMAZON + url.getPath();
        preSignedAWS.setUrl(urlRes);
        return preSignedAWS;
    }

    @Override
    public String uploadFileTos3bucket(String fileName, File file) {
        s3client.putObject(new PutObjectRequest(this.fastoProperties.getS3().getAws().getBucketName(), fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return s3client.getUrl(this.fastoProperties.getS3().getAws().getBucketName(), fileName).toString();
    }

    @Override
    public String uploadFile(MultipartFile multipartFile, String folderType) {
        String fileUrl = "";
        try {
            File file = convertMultiPartToFile(multipartFile);
            String fileName = generateFileName(multipartFile, folderType);
            fileUrl = uploadFileTos3bucket(fileName, file);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileUrl;
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        }
        return convFile;
    }

    private String generateFileName(MultipartFile multiPart, String folderType) {
        return Constants.AWS_PHOTO_FOLDER + folderType.toLowerCase(Locale.ROOT) + Constants.SLASH +
                new Date().getTime() + Constants.CHAR_HYPHEN +
                Objects.requireNonNull(multiPart.getOriginalFilename()).replace(Constants.SPACE, Constants.UNDER_LINE);
    }

    @Override
    public List<PreSignedURL> uploadImagesWithPreSigned(List<ImageUploadDto> imageUploadDtos) throws FastoAlertException {
        if (null != imageUploadDtos && !imageUploadDtos.isEmpty()) {
            List<PreSignedURL> preSignedAWSResponse = new ArrayList<>();
            imageUploadDtos.forEach(imageUploadDTO -> {
                String fileName = imageUploadDTO.getFileName().substring(imageUploadDTO.getFileName().lastIndexOf("/") + 1) + "_" + new Date().getTime() + "." + imageUploadDTO.getFileType();
                switch (imageUploadDTO.getImageFolderType()) {
                    case PRODUCT:
                        fileName = Constants.AWS_PRODUCT_IMAGE_FOLDER + fileName;
                        break;
                    case SHOP:
                        fileName = Constants.AWS_SHOP_IMAGE_FOLDER + fileName;
                        break;
                    case VOUCHER:
                        fileName = Constants.AWS_VOUCHER_IMAGE_FOLDER + fileName;
                        break;
                    case USER:
                        fileName = Constants.AWS_USER_IMAGE_FOLDER + fileName;
                        break;
                    case CATEGORY:
                        fileName = Constants.AWS_CATEGORY_IMAGE_FOLDER + fileName;
                        break;
                    case BILL:
                        fileName = Constants.AWS_BILL_IMAGE_FOLDER + fileName;
                        break;
                    case NEWS:
                        fileName = Constants.AWS_NEWS_IMAGE_FOLDER + fileName;
                        break;
                    case REVIEW:
                        fileName = Constants.AWS_REVIEW_IMAGE_FOLDER + fileName;
                        break;
                }
                PreSignedURL preSignedAWS = this.uploadPreSignedUrl(fileName);
                preSignedAWSResponse.add(preSignedAWS);
            });
            return preSignedAWSResponse;
        } else {
            String codeMessage = messageService.getMessage("error.code.aws.upload.failed");
            String message = messageService.getMessage("error.aws.upload.failed");
            throw new FastoAlertException(codeMessage, message);
        }

    }

    @Override
    public List<String> uploadFiles(List<MultipartFile> multipartFiles, String folderType) throws FastoAlertException {
        if (null != multipartFiles && !multipartFiles.isEmpty()) {
            List<String> response = new ArrayList<>();
            multipartFiles.forEach(multipartFile -> response.add(this.uploadFile(multipartFile, folderType)));
            return response;
        } else {
            String codeMessage = messageService.getMessage("error.code.aws.upload.failed");
            String message = messageService.getMessage("error.aws.upload.failed");
            throw new FastoAlertException(codeMessage, message);
        }

    }

}
