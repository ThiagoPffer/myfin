package com.thpiffer.myfin.app.mapper;

import com.thpiffer.myfin.app.dto.MovementCreateRequest;
import com.thpiffer.myfin.app.dto.MovementResponse;
import com.thpiffer.myfin.app.dto.MovementUpdateRequest;
import com.thpiffer.myfin.app.dto.WalletResponse;
import com.thpiffer.myfin.app.entity.MovementEntity;
import com.thpiffer.myfin.app.entity.WalletEntity;
import com.thpiffer.myfin.app.enumeration.EnumMovementStatus;
import com.thpiffer.myfin.app.mapper.impl.MovementMapperImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MovementMapperTest {

    @InjectMocks
    private MovementMapperImpl mapper;
    @Mock
    private WalletMapper walletMapper;

    @Test
    void toResponse_receivesNull_returnsNull() {
        assertNull(assertDoesNotThrow(() -> mapper.toResponse(null)));
    }

    @Test
    void toResponse_receivesValidEntity_returnsAsResponse() {
        WalletEntity walletEntity = new WalletEntity();
        MovementEntity entity = MovementEntity.builder()
                .id(UUID.fromString("694b2b11-d6b1-4ef7-914e-35a970309fe6"))
                .code(1123)
                .value(BigDecimal.TEN)
                .description("Descricao")
                .status(EnumMovementStatus.COMPLETED)
                .wallet(walletEntity)
                .movementDate(LocalDate.parse("2024-04-01"))
                .build();

        Mockito.when(walletMapper.toResponse(walletEntity))
                .thenReturn(Mockito.mock(WalletResponse.class));

        MovementResponse result = assertDoesNotThrow(() -> mapper.toResponse(entity));

        assertNotNull(result);
        assertEquals(entity.getId(), result.id());
        assertEquals(entity.getCode(), result.code());
        assertEquals(entity.getValue(), result.value());
        assertEquals(entity.getDescription(), result.description());
        assertEquals(entity.getStatus(), result.status());
        assertEquals(entity.getMovementDate(), result.movementDate());
        assertNotNull(result.wallet());
    }

    @Test
    void fromCreateRequest_receivesNull_returnsNull() {
        assertNull(assertDoesNotThrow(() -> mapper.fromCreateRequest(null, null)));
    }

    @Test
    void fromCreateRequest_receivesValidCreateRequest_returnsEntity() {
        WalletEntity wallet = WalletEntity.builder()
                .id(UUID.fromString("694b2b11-d6b1-4ef7-914e-35a970309fe6"))
                .build();

        MovementCreateRequest request = new MovementCreateRequest(
                "Descricao",
                BigDecimal.TEN,
                wallet.getId(),
                LocalDate.parse("2024-04-01")
        );

        MovementEntity result = assertDoesNotThrow(() -> mapper.fromCreateRequest(request, wallet));

        assertNotNull(result);
        assertEquals(request.description(), result.getDescription());
        assertEquals(request.value(), result.getValue());
        assertEquals(request.walletId(), result.getWallet().getId());
        assertEquals(request.movementDate(), result.getMovementDate());
        assertEquals(EnumMovementStatus.PENDING, result.getStatus());
    }

    @Test
    void fromUpdateRequest_receivesNull_returnsEntity() {
        MovementEntity entity = MovementEntity.builder()
                .id(UUID.fromString("694b2b11-d6b1-4ef7-914e-35a970309fe6"))
                .description("Descricao")
                .value(BigDecimal.TEN)
                .movementDate(LocalDate.parse("2024-04-01"))
                .build();

        MovementEntity result = assertDoesNotThrow(() ->
                mapper.fromUpdateRequest(entity, null, null));

        assertNotNull(result);
        assertEquals("Descricao", result.getDescription());
        assertEquals(BigDecimal.TEN, result.getValue());
        assertEquals(LocalDate.parse("2024-04-01"), result.getMovementDate());
    }

    @Test
    void fromUpdateRequest_receivesValidUpdateRequest_returnsEntity() {
        WalletEntity wallet = WalletEntity.builder()
                .id(UUID.fromString("694b2b11-d6b1-4ef7-914e-35a970309fe6"))
                .build();

        MovementEntity entity = MovementEntity.builder()
                .id(UUID.fromString("694b2b11-d6b1-4ef7-914e-35a970309fe7"))
                .description("Descricao Antiga")
                .value(BigDecimal.ONE)
                .movementDate(LocalDate.parse("2024-04-01"))
                .build();

        MovementUpdateRequest request = new MovementUpdateRequest(
                "Descricao Nova",
                BigDecimal.valueOf(100),
                wallet.getId(),
                LocalDate.parse("2024-04-02")
        );

        MovementEntity result = assertDoesNotThrow(() ->
                mapper.fromUpdateRequest(entity, request, wallet));

        assertNotNull(result);
        assertEquals("Descricao Nova", result.getDescription());
        assertEquals(BigDecimal.valueOf(100), result.getValue());
        assertEquals(wallet.getId(), result.getWallet().getId());
        assertEquals(LocalDate.parse("2024-04-02"), result.getMovementDate());
    }

}