package com.thpiffer.myfin.app.resource;

import com.thpiffer.myfin.app.dto.MovementCreateRequest;
import com.thpiffer.myfin.app.dto.MovementResponse;
import com.thpiffer.myfin.app.dto.MovementUpdateRequest;
import com.thpiffer.myfin.app.entity.MovementEntity;
import com.thpiffer.myfin.app.exception.NotFoundException;
import com.thpiffer.myfin.app.mapper.MovementMapper;
import com.thpiffer.myfin.app.resource.impl.MovementResourceImpl;
import com.thpiffer.myfin.app.service.MovementService;
import com.thpiffer.myfin.core.dto.PagingRequest;
import com.thpiffer.myfin.core.dto.ScrollingOutput;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class MovementResourceTest {

    @InjectMocks
    private MovementResourceImpl resource;
    @Mock
    private MovementService service;
    @Mock
    private MovementMapper mapper;

    @Test
    void getMovementListByCompetence_receivesValidCompetenceString_returnsListOfMovements() {
        String competence = "2024-06";
        MovementEntity entity = new MovementEntity();
        MovementResponse response = Mockito.mock(MovementResponse.class);

        Mockito.when(service.getMovementListByCompetence(competence))
                .thenReturn(List.of(entity));
        Mockito.when(mapper.toResponse(entity))
                .thenReturn(response);

        var result = Assertions.assertDoesNotThrow(() ->
                resource.getMovementListByCompetence(competence));

        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBody());
        Assertions.assertFalse(result.getBody().isEmpty());

        Mockito.verify(service).getMovementListByCompetence(competence);
        Mockito.verify(mapper).toResponse(entity);
    }

    @Test
    void getMovementListByCompetence_receivesValidCompetenceString_returnsEmptyList() {
        String competence = "2024-06";

        Mockito.when(service.getMovementListByCompetence(competence))
                .thenReturn(List.of());

        var result = Assertions.assertDoesNotThrow(() ->
                resource.getMovementListByCompetence(competence));

        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBody());
        Assertions.assertTrue(result.getBody().isEmpty());

        Mockito.verify(service).getMovementListByCompetence(competence);
    }

    @Test
    void getMovementScrollPageByFilter_receivesValidRequest_returnsMovementScrollPage() {
        PagingRequest request = new PagingRequest(1, 10, "filter");
        ScrollingOutput<MovementEntity> output = new ScrollingOutput<>(
                true, List.of(new MovementEntity()));

        Mockito.when(service.getMovementScrollPageByFilter(
                PageRequest.of(request.page(), request.size()), request.filter()))
                .thenReturn(output);
        Mockito.when(mapper.toResponse(output.content().getFirst()))
                .thenReturn(Mockito.mock(MovementResponse.class));

        var result = Assertions.assertDoesNotThrow(() ->
                resource.getMovementScrollPageByFilter(request));

        Assertions.assertNotNull(result);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertNotNull(result.getHeaders());
        Assertions.assertFalse(result.getHeaders().isEmpty());
        Assertions.assertNotNull(result.getHeaders().get("X-Has-Next"));
        Assertions.assertNotNull(result.getBody());
        Assertions.assertFalse(result.getBody().isEmpty());

        Mockito.verify(service).getMovementScrollPageByFilter(
                PageRequest.of(request.page(), request.size()), request.filter());
        Mockito.verify(mapper).toResponse(output.content().getFirst());
    }

    @Test
    void getMovementScrollPageByFilter_receivesValidRequest_returnsEmptyList() {
        PagingRequest request = new PagingRequest(1, 10, "filter");
        ScrollingOutput<MovementEntity> output = new ScrollingOutput<>(
                false, List.of());

        Mockito.when(service.getMovementScrollPageByFilter(
                PageRequest.of(request.page(), request.size()), request.filter()))
                .thenReturn(output);

        var result = Assertions.assertDoesNotThrow(() ->
                resource.getMovementScrollPageByFilter(request));

        Assertions.assertNotNull(result);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertNotNull(result.getHeaders());
        Assertions.assertFalse(result.getHeaders().isEmpty());
        Assertions.assertNotNull(result.getHeaders().get("X-Has-Next"));
        Assertions.assertNotNull(result.getBody());
        Assertions.assertTrue(result.getBody().isEmpty());

        Mockito.verify(service).getMovementScrollPageByFilter(
                PageRequest.of(request.page(), request.size()), request.filter());
        Mockito.verifyNoInteractions(mapper);
    }

    @Test
    void getMovementById_receivesValidId_returnsMovementResponse() {
        UUID id = UUID.fromString("b96e2841-9002-4d37-bbdb-a628112ebd03");
        MovementEntity entity = new MovementEntity();
        MovementResponse response = Mockito.mock(MovementResponse.class);

        Mockito.when(service.getMovementById(id)).thenReturn(Optional.of(entity));
        Mockito.when(mapper.toResponse(entity)).thenReturn(response);

        var result = Assertions.assertDoesNotThrow(() ->
                resource.getMovementById(id));

        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBody());

        Mockito.verify(service).getMovementById(id);
        Mockito.verify(mapper).toResponse(entity);
    }

    @Test
    void getMovementById_receivesNonExistentId_throwsNotFoundException() {
        UUID id = UUID.fromString("b96e2841-9002-4d37-bbdb-a628112ebd03");

        Mockito.when(service.getMovementById(id)).thenReturn(Optional.empty());

        var exception = Assertions.assertThrows(NotFoundException.class, () ->
                resource.getMovementById(id));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(NotFoundException.class, exception.getClass());

        Mockito.verify(service).getMovementById(id);
        Mockito.verifyNoInteractions(mapper);
    }

    @Test
    void createMovement_receivesValidRequestBody_returnsMovementResponseWithCreatedStatus() {
        MovementCreateRequest request = new MovementCreateRequest(
                "Salário",
                BigDecimal.valueOf(10000L),
                UUID.fromString("b96e2841-9002-4d37-bbdb-a628112ebd03"),
                LocalDate.of(2026, 1, 5)
        );
        MovementEntity entity = new MovementEntity();
        MovementResponse response = Mockito.mock(MovementResponse.class);

        Mockito.when(service.createMovement(request)).thenReturn(entity);
        Mockito.when(mapper.toResponse(entity)).thenReturn(response);

        var result = Assertions.assertDoesNotThrow(() ->
                resource.createMovement(request));

        Assertions.assertEquals(HttpStatus.CREATED, result.getStatusCode());
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBody());
    }

    @Test
    void updateMovement_receivesValidRequestBody_returnsMovementResponseWithUpdatedDescription() {
        String newDescription = "Novo Movimento X";
        UUID movementId = UUID.fromString("b96e2841-9002-4d37-bbdb-a628112ebd03");
        MovementUpdateRequest request = new MovementUpdateRequest(
                "Movimento X",
                BigDecimal.TEN,
                movementId,
                LocalDate.of(2026, 1, 5)
        );
        MovementEntity entity = Mockito.mock(MovementEntity.class);
        MovementResponse response = Mockito.mock(MovementResponse.class);

        Mockito.when(service.updateMovement(Mockito.any(UUID.class), Mockito.any(MovementUpdateRequest.class)))
                .thenReturn(entity);
        Mockito.when(mapper.toResponse(entity)).thenReturn(response);
        Mockito.when(response.description()).thenReturn(newDescription);

        var output = Assertions.assertDoesNotThrow(() -> resource.updateMovement(movementId, request));

        Assertions.assertEquals(HttpStatus.OK, output.getStatusCode());
        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.getBody());
        Assertions.assertEquals(newDescription, output.getBody().description());

        Mockito.verify(service).updateMovement(movementId, request);
        Mockito.verify(mapper).toResponse(entity);
    }

    @Test
    void deleteMovement_receivesValidId_returnsNoContent() {
        UUID id = UUID.fromString("b96e2841-9002-4d37-bbdb-a628112ebd03");

        var result = Assertions.assertDoesNotThrow(() ->
                resource.deleteMovement(id));

        Assertions.assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        Assertions.assertNull(result.getBody());

        Mockito.verify(service).deleteMovementById(id);
        Mockito.verifyNoInteractions(mapper);
    }

}
