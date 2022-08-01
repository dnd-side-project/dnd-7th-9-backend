package dnd.studyplanner.controller;

import java.nio.charset.Charset;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dnd.studyplanner.config.Message;
import dnd.studyplanner.config.StatusCode;
import dnd.studyplanner.service.IImageUploadService;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/s3")
@RequiredArgsConstructor
public class ImageUploadController {

	private final IImageUploadService imageUploadService;

	@PostMapping("/file")
	public ResponseEntity<Message> upload(@ApiParam(value="여러 파일 업로드 가능", required = true) @RequestPart List<MultipartFile> multipartFile) throws Exception {

		List<String> fileImageList = imageUploadService.upload(multipartFile);

		Message message = new Message();
		HttpHeaders headers= new HttpHeaders();
		headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

		message.setStatus(StatusCode.OK);
		message.setMessage("이미지 업로드 성공");
		message.setData(fileImageList);

		return new ResponseEntity<>(message, headers, HttpStatus.OK);

	}

	@DeleteMapping("/file")
	public ResponseEntity<Message> remove(String fileName) throws Exception {

		imageUploadService.remove(fileName);

		Message message = new Message();
		HttpHeaders headers= new HttpHeaders();
		headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

		message.setStatus(StatusCode.OK);
		message.setMessage("이미지 삭제 성공");
		message.setData(null);

		return new ResponseEntity<>(message, headers, HttpStatus.OK);
	}
}
