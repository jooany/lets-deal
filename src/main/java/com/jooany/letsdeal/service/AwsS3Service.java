package com.jooany.letsdeal.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.jooany.letsdeal.config.AwsConfig;
import com.jooany.letsdeal.exception.ErrorCode;
import com.jooany.letsdeal.exception.LetsDealAppException;
import com.jooany.letsdeal.util.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Service
public class AwsS3Service {
    private final AwsConfig awsConfig;
    private final AmazonS3 s3Client;


    public String saveImageToS3 (MultipartFile file) throws IOException {

        String convertedFileName = FileUtils.convertFileName(file.getOriginalFilename());

        try {
            // 이미지 mimeType 유효성 체크
            String mimeType = new Tika().detect(file.getInputStream());
            FileUtils.checkImageMimeType(mimeType);

            // S3 버킷 및 키 설정
            String bucketName = awsConfig.getS3().getBucketName();
            String key = awsConfig.getS3().getFolderName() + "/" + convertedFileName;

            // 메타데이터 설정
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(mimeType);

            // 파일 업로드
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file.getInputStream(), metadata);
            s3Client.putObject(putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead));

            // 업로드된 파일의 URL 반환
            return s3Client.getUrl(bucketName, key).toString();

        } catch (IOException e) {
            log.error("S3 이미지 업로드 에러 발생: {}", e.getMessage());
            throw new LetsDealAppException(ErrorCode.IMAGE_UPLOAD_FAIL);
        }
    }
}
