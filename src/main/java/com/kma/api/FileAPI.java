package com.kma.api;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import com.kma.constants.fileDirection;
import com.kma.models.errorResponseDTO;
import com.kma.models.fileDTO;
import com.kma.repository.entities.NhanVien;
import com.kma.repository.entities.SinhVien;
import com.kma.repository.nhanVienRepo;
import com.kma.repository.sinhVienRepo;
import com.kma.services.fileService;
import com.kma.utilities.buildErrorResUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kma.utilities.fileDownloadUtil;
import org.springframework.web.multipart.MultipartFile;


@RestController
public class FileAPI {

	@Autowired
	sinhVienRepo svRepo;
	@Autowired
	nhanVienRepo nvRepo;
	@Autowired
	fileService fileServ;
	@Autowired
	buildErrorResUtil buildErrorResUtil;

	@DeleteMapping("/image/{imgId}")
	public ResponseEntity<?> deleteImg(@PathVariable Integer imgId) {
		try{
			fileServ.deleteImg(imgId);
			return new ResponseEntity<>("Delete successfully!", HttpStatus.OK);
		} catch (Exception e){
			errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
			return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/uploadImg")
	public ResponseEntity<?> uploadImg(@RequestParam(value = "file", required = false) MultipartFile multipartFile) {
		try{
			fileDTO dto = fileServ.uploadImg(multipartFile);
			return new ResponseEntity<>(dto, HttpStatus.OK);
		} catch (Exception e){
			errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
			return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@GetMapping("/downloadFile/{fileCode}")
	public ResponseEntity<?> downloadFile(@PathVariable("fileCode") String fileCode) {
	    String path = fileDirection.pathForTaiNguyen;
		return getFileResponse(fileCode, path);
	}

	@GetMapping("/downloadDocs/{fileCode}")
	public ResponseEntity<?> downloadDocs(@PathVariable("fileCode") String fileCode) {
		String path = fileDirection.pathForTaiLieuMonHoc;
		return getFileResponse(fileCode, path);
	}

	@GetMapping("/downloadProfile/{fileCode}")
	public ResponseEntity<?> downloadProfile(@PathVariable("fileCode") String fileCode) {
		String directoryPath;

		// Tìm kiếm fileCode trong SinhVien và NhanVien
		SinhVien sinhVien = svRepo.findByAvaFileCode(fileCode);
		if (sinhVien != null) {
			directoryPath = fileDirection.pathForProfile_SV + "/" + sinhVien.getLop().getTenLop() + "/" + sinhVien.getMaSinhVien();
		} else {
			NhanVien nhanVien = nvRepo.findByAvaFileCode(fileCode);
			if (nhanVien != null) {
				directoryPath = fileDirection.pathForProfile_NV + "/" + nhanVien.getPhongBan().getMaPhongBan() + "/" + nhanVien.getMaNhanVien();
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy file!");
			}
		}
		return getFileResponse(fileCode, directoryPath);
	}

	private ResponseEntity<?> getFileResponse(String fileCode, String path){
		fileDownloadUtil downloadUtil = new fileDownloadUtil();

		Resource resource;
		try {
			resource = downloadUtil.getFileAsResource(fileCode, path);
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