package com.thpiffer.myfin.app.service;

import com.thpiffer.myfin.app.dto.MovementCreateRequest;
import com.thpiffer.myfin.app.dto.MovementUpdateRequest;
import com.thpiffer.myfin.app.entity.MovementEntity;
import com.thpiffer.myfin.app.entity.WalletEntity;
import com.thpiffer.myfin.app.exception.NotFoundException;
import com.thpiffer.myfin.app.mapper.MovementMapper;
import com.thpiffer.myfin.app.repository.MovementRepository;
import com.thpiffer.myfin.app.service.impl.MovementServiceImpl;
import com.thpiffer.myfin.core.dto.ScrollingOutput;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class MovementServiceTest {

    @InjectMocks
    private MovementServiceImpl service;
    @Mock
    private WalletService walletService;
    @Mock
    private MovementRepository repository;
    @Mock
    private MovementMapper mapper;

    @Test
    void getMovementListByCompetence_receivesValidCompetence_returnsListOfMovements() {
        String competence = "2024-05";
        MovementEntity entity = new MovementEntity();

        Mockito.when(repository.findByMovementDateBetween(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of(entity));

        List<MovementEntity> result = Assertions.assertDoesNotThrow(() ->
                service.getMovementListByCompetence(competence));

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    void getMovementScrollPageByFilter_receivesValidRequest_returnsScrollPage() {
        String filter = "filter";
        PageRequest request = PageRequest.of(0, 10);

        Mockito.when(repository.findAll(filter, request))
                .thenReturn(new SliceImpl<>(List.of(new MovementEntity()), request, true));

        ScrollingOutput<MovementEntity> result = Assertions.assertDoesNotThrow(
                () -> service.getMovementScrollPageByFilter(request, filter));

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.content());
        Assertions.assertFalse(result.content().isEmpty());
        Assertions.assertTrue(result.hasNext());
    }

    @Test
    void getMovementById_receivesValidId_returnsMovementOptional() {
        UUID id = UUID.fromString("3dc4c341-3b30-4f70-9ea8-759bace4fed9");
        MovementEntity entity = new MovementEntity();
        entity.setId(id);

        Mockito.when(repository.findById(id)).thenReturn(Optional.of(entity));

        Optional<MovementEntity> result = Assertions.assertDoesNotThrow(() ->
                service.getMovementById(id));

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(id, result.get().getId());
    }

    @Test
    void createMovement_receivesValidPayload_createsAndReturnsEntity() {
        MovementCreateRequest request = Mockito.mock(MovementCreateRequest.class);
        WalletEntity wallet = new WalletEntity();
        MovementEntity entity = MovementEntity.builder()
                .id(UUID.fromString("3dc4c341-3b30-4f70-9ea8-759bace4fed9"))
                .build();

        Mockito.when(walletService.getWalletById(request.walletId()))
                        .thenReturn(Optional.of(wallet));
        Mockito.when(mapper.fromCreateRequest(request, wallet)).thenReturn(entity);
        Mockito.when(repository.save(any(MovementEntity.class)))
                .thenReturn(entity);

        MovementEntity result = Assertions.assertDoesNotThrow(() -> service.createMovement(request));

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getId());
    }

    @Test
    void createMovement_receivesValidPayload_returnsNotFoundException() {
        MovementCreateRequest request = Mockito.mock(MovementCreateRequest.class);

        Mockito.when(walletService.getWalletById(request.walletId()))
                        .thenReturn(Optional.empty());

        NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class, () -> service.createMovement(request));

        Assertions.assertNotNull(exception);
    }

    @Test
    void updateMovement_receivesValidIdAndRequest_updatesAndReturnsMovementEntity() {
        UUID movementId = UUID.fromString("3dc4c341-3b30-4f70-9ea8-759bace4fed9");
        UUID walletId = UUID.fromString("5eb9b4a4-a79a-425d-9f57-1d85a5e58a65");
        MovementUpdateRequest request = new MovementUpdateRequest(
                "Movimento Updated",
                BigDecimal.TEN,
                walletId,
                LocalDate.parse("2026-06-06")
        );
        MovementEntity entity = MovementEntity.builder()
                .id(movementId)
                .description("Old Description")
                .value(BigDecimal.ONE)
                .movementDate(LocalDate.parse("2026-06-05"))
                .build();
        WalletEntity wallet = WalletEntity.builder().id(walletId).build();
        MovementEntity updatedEntity = MovementEntity.builder()
                .id(movementId)
                .description("Movimento Updated")
                .value(BigDecimal.TEN)
                .wallet(wallet)
                .movementDate(LocalDate.parse("2026-06-06"))
                .build();

        Mockito.when(repository.findById(movementId)).thenReturn(Optional.of(entity));
        Mockito.when(walletService.getWalletById(walletId)).thenReturn(Optional.of(wallet));
        Mockito.when(mapper.fromUpdateRequest(entity, request, wallet)).thenReturn(updatedEntity);
        Mockito.when(repository.save(updatedEntity)).thenReturn(updatedEntity);

        MovementEntity result = Assertions.assertDoesNotThrow(() -> 
                service.updateMovement(movementId, request));

        Assertions.assertNotNull(result);
        Assertions.assertEquals(movementId, result.getId());
        Assertions.assertEquals("Movimento Updated", result.getDescription());
        Assertions.assertEquals(BigDecimal.TEN, result.getValue());

        Mockito.verify(repository, Mockito.times(1)).findById(movementId);
        Mockito.verify(walletService, Mockito.times(1)).getWalletById(walletId);
        Mockito.verify(repository, Mockito.times(1)).save(updatedEntity);
    }

    @Test
    void updateMovement_receivesInvalidMovementId_throwsNotFoundException() {
        UUID movementId = UUID.fromString("3dc4c341-3b30-4f70-9ea8-759bace4fed9");
        UUID walletId = UUID.fromString("5eb9b4a4-a79a-425d-9f57-1d85a5e58a65");
        MovementUpdateRequest request = new MovementUpdateRequest(
                "Movimento Updated",
                BigDecimal.TEN,
                walletId,
                LocalDate.parse("2026-06-06")
        );

        Mockito.when(repository.findById(movementId)).thenReturn(Optional.empty());

        NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class, () -> service.updateMovement(movementId, request));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Movement not found with id: " + movementId, exception.getMessage());

        Mockito.verify(repository, Mockito.times(1)).findById(movementId);
    }

    @Test
    void updateMovement_receivesInvalidWalletId_throwsNotFoundException() {
        UUID movementId = UUID.fromString("3dc4c341-3b30-4f70-9ea8-759bace4fed9");
        UUID walletId = UUID.fromString("5eb9b4a4-a79a-425d-9f57-1d85a5e58a65");
        MovementUpdateRequest request = new MovementUpdateRequest(
                "Movimento Updated",
                BigDecimal.TEN,
                walletId,
                LocalDate.parse("2026-06-06")
        );
        MovementEntity entity = MovementEntity.builder()
                .id(movementId)
                .description("Old Description")
                .value(BigDecimal.ONE)
                .build();

        Mockito.when(repository.findById(movementId)).thenReturn(Optional.of(entity));
        Mockito.when(walletService.getWalletById(walletId)).thenReturn(Optional.empty());

        NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class, () -> service.updateMovement(movementId, request));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Wallet Not Found", exception.getMessage());

        Mockito.verify(repository, Mockito.times(1)).findById(movementId);
        Mockito.verify(walletService, Mockito.times(1)).getWalletById(walletId);
    }

    @Test
    void deleteMovementById_receivesValidId_deletesMovement() {
        UUID movementId = UUID.fromString("3dc4c341-3b30-4f70-9ea8-759bace4fed9");

        Mockito.doNothing().when(repository).deleteById(movementId);

        Assertions.assertDoesNotThrow(() -> service.deleteMovementById(movementId));

        Mockito.verify(repository, Mockito.times(1)).deleteById(movementId);
    }

}
