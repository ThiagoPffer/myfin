package com.thpiffer.myfin.app.resource.impl;

import com.thpiffer.myfin.app.dto.MovementCreateRequest;
import com.thpiffer.myfin.app.dto.MovementResponse;
import com.thpiffer.myfin.app.dto.MovementUpdateRequest;
import com.thpiffer.myfin.app.exception.NotFoundException;
import com.thpiffer.myfin.app.mapper.MovementMapper;
import com.thpiffer.myfin.app.resource.MovementResource;
import com.thpiffer.myfin.app.service.MovementService;
import com.thpiffer.myfin.core.dto.PagingRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/movements")
@RequiredArgsConstructor
public class MovementResourceImpl implements MovementResource {

    private final MovementService service;
    private final MovementMapper mapper;

    @Override
    @GetMapping
    @RequestMapping("/competence/{competence}")
    public ResponseEntity<List<MovementResponse>> getMovementListByCompetence(@PathVariable String competence) {
        var output = service.getMovementListByCompetence(competence);
        return ResponseEntity.ok()
                .body(output.stream().map(mapper::toResponse).toList());
    }

    @Override
    @GetMapping
    public ResponseEntity<List<MovementResponse>> getMovementScrollPageByFilter(PagingRequest request) {
        var output = service.getMovementScrollPageByFilter(
                PageRequest.of(request.page(), request.size()), request.filter());
        return ResponseEntity.ok()
                .header("X-Has-Next", String.valueOf(output.hasNext()))
                .body(output.content().stream().map(mapper::toResponse).toList());
    }

    @Override
    @GetMapping
    @RequestMapping("/{id}")
    public ResponseEntity<MovementResponse> getMovementById(@PathVariable UUID id) {
        var output = service.getMovementById(id).orElseThrow(
                () -> new NotFoundException("Movement not found with id: " + id));
        return ResponseEntity.ok().body(mapper.toResponse(output));
    }

    @Override
    @PostMapping
    public ResponseEntity<MovementResponse> createMovement(@RequestBody MovementCreateRequest request) {
        var result = service.createMovement(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.toResponse(result));
    }

    @Override
    @PutMapping
    @RequestMapping("/{id}")
    public ResponseEntity<MovementResponse> updateMovement(
            @PathVariable UUID id, @RequestBody MovementUpdateRequest request) {
        var result = service.updateMovement(id, request);
        return ResponseEntity.ok().body(mapper.toResponse(result));
    }

    @Override
    @DeleteMapping
    @RequestMapping("/{id}")
    public ResponseEntity<Void> deleteMovement(UUID id) {
        service.deleteMovementById(id);
        return ResponseEntity.noContent().build();
    }

}
