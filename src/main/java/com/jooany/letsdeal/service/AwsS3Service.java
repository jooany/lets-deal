package com.jooany.letsdeal.service;

import java.io.IOException;

import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

@Slf4j
@RequiredArgsConstructor
@Service
public class AwsS3Service {
	private final AwsConfig awsConfig;
	private final AmazonS3 s3Client;

	public String saveImageToS3(MultipartFile file) throws IOException {

		String convertedFileName = FileUtils.convertFileName(file.getOriginalFilename());

		try {
			String mimeType = new Tika().detect(file.getInputStream());
			FileUtils.checkImageMimeType(mimeType);

			String bucketName = awsConfig.getS3().getBucketName();
			String key = awsConfig.getS3().getFolderName() + "/" + convertedFileName;

			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentType(mimeType);

			PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file.getInputStream(), metadata);
			s3Client.putObject(putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead));

			return awsConfig.getCloudFrontDomain() + "/" + key;

		} catch (IOException e) {
			log.error("S3 이미지 업로드 에러 발생: {}", e.getMessage());
			throw new LetsDealAppException(ErrorCode.IMAGE_UPLOAD_FAIL);
		}
	}

	public void deleteImage(String imageUrl) {
		String fileName = imageUrl.substring(imageUrl.lastIndexOf("/"));
		String key = awsConfig.getS3().getFolderName() + fileName;
		s3Client.deleteObject(awsConfig.getS3().getBucketName(), key);
	}
}
