package com.thpiffer.myfin.app.service;

import com.thpiffer.myfin.app.dto.MovementCreateRequest;
import com.thpiffer.myfin.app.dto.MovementUpdateRequest;
import com.thpiffer.myfin.app.entity.MovementEntity;
import com.thpiffer.myfin.core.dto.ScrollingOutput;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MovementService {

    List<MovementEntity> getMovementListByCompetence(String competence);

    ScrollingOutput<MovementEntity> getMovementScrollPageByFilter(PageRequest page, String filter);

    Optional<MovementEntity> getMovementById(UUID id);

    MovementEntity createMovement(MovementCreateRequest request);

    MovementEntity updateMovement(UUID id, MovementUpdateRequest updateRequest);

    void deleteMovementById(UUID id);

}
