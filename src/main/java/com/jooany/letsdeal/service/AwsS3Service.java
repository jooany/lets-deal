package com.jooany.letsdeal.service;

import java.io.IOException;
import java.util.UUID;

import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jooany.letsdeal.config.AwsConfig;
import com.jooany.letsdeal.exception.ErrorCode;
import com.jooany.letsdeal.exception.LetsDealAppException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j
@RequiredArgsConstructor
@Service
public class AwsS3Service {

	private final AwsConfig awsConfig;
	private final S3Client s3Client;
	private final Tika tika = new Tika();

	public String saveImageToS3(MultipartFile file) {
		String originalFileName = file.getOriginalFilename();
		String convertedFileName = UUID.randomUUID() + "_" + originalFileName;
		String bucket = awsConfig.getS3().getBucketName();
		String key = awsConfig.getS3().getFolderName() + "/" + convertedFileName;

		try {
			String mimeType = tika.detect(file.getInputStream());

			PutObjectRequest request = PutObjectRequest.builder()
				.bucket(bucket)
				.key(key)
				.contentType(mimeType)
				.acl("public-read")
				.build();

			s3Client.putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
			return awsConfig.getCloudFrontDomain() + "/" + key;

		} catch (IOException | AwsServiceException e) {
			log.error("S3 이미지 업로드 실패: {}", e.getMessage(), e);
			throw new LetsDealAppException(ErrorCode.IMAGE_UPLOAD_FAIL);
		}
	}

	public void deleteImage(String imageUrl) {
		String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
		String key = awsConfig.getS3().getFolderName() + "/" + fileName;

		DeleteObjectRequest request = DeleteObjectRequest.builder()
			.bucket(awsConfig.getS3().getBucketName())
			.key(key)
			.build();

		s3Client.deleteObject(request);
	}
}
