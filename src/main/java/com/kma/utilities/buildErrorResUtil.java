package com.kma.utilities;

import com.kma.models.errorResponseDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class buildErrorResUtil {
    // Hàm tạo ResponseEntity cho lỗi
    public errorResponseDTO buildErrorRes(Exception e, String message) {
        errorResponseDTO errorDTO = new errorResponseDTO();
        errorDTO.setError(e.getMessage());
        List<String> details = new ArrayList<>();
        details.add(message);
        errorDTO.setDetails(details);

        return errorDTO;
    }
}
