package dnd.studyplanner.service.Impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import dnd.studyplanner.service.IImageUploadService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageUploadService implements IImageUploadService {

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	private final AmazonS3 amazonS3;

	@Override
	public List<String> upload(List<MultipartFile> multipartFile) throws Exception {

		List<String> fileNameList = new ArrayList<>();

		multipartFile.forEach(file -> {
			String fileName = createFileName(file.getOriginalFilename());
			ObjectMetadata objectMetadata = new ObjectMetadata();
			objectMetadata.setContentLength(file.getSize());
			objectMetadata.setContentType(file.getContentType());

			try (InputStream inputStream = file.getInputStream()) {
				amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
					.withCannedAcl(CannedAccessControlList.PublicRead));
			} catch (IOException e) {
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드 실패");
			}

			fileNameList.add(fileName);
		});

		return fileNameList;
	}

	private String createFileName(String fileName) {
		// 랜덤으로 파일 이름 생성
		return UUID.randomUUID().toString().concat(getFileExtension(fileName));
	}

	// 파일 확장자 전달
	private String getFileExtension(String fileName) {
		try {
			return fileName.substring(fileName.lastIndexOf("."));
		} catch (StringIndexOutOfBoundsException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일 (" + fileName + ") 입니다.");
		}
	}

	@Override
	public void remove(String fileName) throws Exception {
		amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
	}
}
