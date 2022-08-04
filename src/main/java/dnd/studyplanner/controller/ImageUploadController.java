package dnd.studyplanner.controller;

import static dnd.studyplanner.dto.response.CustomResponseStatus.*;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dnd.studyplanner.dto.response.CustomResponse;
import dnd.studyplanner.service.IImageUploadService;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/s3")
@RequiredArgsConstructor
public class ImageUploadController {

	private final IImageUploadService imageUploadService;

	@PostMapping("/file")
	public ResponseEntity<CustomResponse> upload(@ApiParam(value="여러 파일 업로드 가능", required = true) @RequestPart List<MultipartFile> multipartFile) throws Exception {

		List<String> fileImageList = imageUploadService.upload(multipartFile);

		// Message message = new Message();
		// HttpHeaders headers= new HttpHeaders();
		// headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
		//
		// message.setStatus(StatusCode.OK);
		// message.setMessage("이미지 업로드 성공");
		// message.setData(fileImageList);

		return new CustomResponse<>(fileImageList, UPLOAD_IMAGE_SUCCESS).toResponseEntity();

	}

	@DeleteMapping("/file")
	public ResponseEntity<CustomResponse> remove(String fileName) throws Exception {

		imageUploadService.remove(fileName);

		// Message message = new Message();
		// HttpHeaders headers= new HttpHeaders();
		// headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
		//
		// message.setStatus(StatusCode.OK);
		// message.setMessage("이미지 삭제 성공");
		// message.setData(null);

		return new CustomResponse<>(DELETE_IMAGE_SUCCESS).toResponseEntity();
	}
}
