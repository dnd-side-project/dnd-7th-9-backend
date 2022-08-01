package dnd.studyplanner.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface IImageUploadService {

	List<String> upload(List<MultipartFile> multipartFile) throws Exception;

	void remove(String fileName) throws Exception;
}
