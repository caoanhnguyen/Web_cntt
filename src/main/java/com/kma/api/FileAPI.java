package com.kma.api;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.kma.utilities.fileDownloadUtil;


@RestController
public class FileAPI {
	@GetMapping("/downloadFile/{fileCode}")
	public ResponseEntity<?> downloadFile(@PathVariable("fileCode") String fileCode) {
	    fileDownloadUtil downloadUtil = new fileDownloadUtil();
	    
	    Resource resource = null;
	    try {
	        resource = downloadUtil.getFileAsResource(fileCode);
	    } catch (IOException e) {
	        return ResponseEntity.internalServerError().build();
	    }

	    if (resource == null) {
	        return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
	    }else{
			String contentType = getContentType(Objects.requireNonNull(resource.getFilename())); // Hàm lấy MIME type từ file
			String encodedFileName = new String(resource.getFilename().getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
			String headerValue = "attachment; filename=\"" + encodedFileName + "\"";

			return ResponseEntity.ok()
					.contentType(MediaType.parseMediaType(contentType))
					.header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
					.header("Access-Control-Expose-Headers", HttpHeaders.CONTENT_DISPOSITION)
					.body(resource);
		}
	}

	// Hàm xác định MIME type dựa trên phần mở rộng file
	private String getContentType(String filename) {
		if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
			return "image/jpeg";
		} else if (filename.endsWith(".png")) {
			return "image/png";
		} else if (filename.endsWith(".gif")) {
			return "image/gif";
		} else if (filename.endsWith(".pdf")) {
			return "application/pdf";
		} else if (filename.endsWith(".txt")) {
			return "text/plain";
		} else if (filename.endsWith("docx") || filename.endsWith("doc")) {
			return "application/docx";
		} else if (filename.endsWith("xlsx") || filename.endsWith("xls")) {
			return "application/xlsx";
		} else if (filename.endsWith("pptx") || filename.endsWith("ppt")) {
			return "application/pptx";
		} else {
			return "application/octet-stream"; // Đối với các loại file khác
		}
	}
}