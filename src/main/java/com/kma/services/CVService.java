package com.kma.services;

import com.kma.models.CVDTO;

public interface CVService {
    CVDTO getByUserId(Integer userId);

    void addCV(CVDTO cvDTO, Integer idUser);

    void updateCV(Integer CVId, CVDTO cvDTO);

    void deleteCV(Integer CVId);

    boolean isOwner(Integer CVId, Integer idUser);
}
