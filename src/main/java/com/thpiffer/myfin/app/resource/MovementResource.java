package com.thpiffer.myfin.app.resource;

import com.thpiffer.myfin.app.dto.*;
import com.thpiffer.myfin.core.dto.PagingRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface MovementResource {

    ResponseEntity<List<MovementResponse>> getMovementListByCompetence(String competence);

    ResponseEntity<List<MovementResponse>> getMovementScrollPageByFilter(PagingRequest request);

    ResponseEntity<MovementResponse> getMovementById(UUID id);

    ResponseEntity<MovementResponse> createMovement(MovementCreateRequest request);

    ResponseEntity<MovementResponse> updateMovement(UUID id, MovementUpdateRequest request);

    ResponseEntity<Void> deleteMovement(UUID id);

}
