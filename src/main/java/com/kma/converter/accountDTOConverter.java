package com.kma.converter;

import com.kma.models.accountDTO;
import org.springframework.stereotype.Component;

@Component
public class accountDTOConverter {

    public accountDTO convertToAccDTO(Object[] row){
        accountDTO dto = new accountDTO();

        dto.setAccountId((Integer) row[0]);
        dto.setUserName((String) row[1]);
        dto.setLocked((boolean) row[2]);
        dto.setEntityId((String) row[3]);
        dto.setFullName((String) row[4]);

        return dto;
    }
}
