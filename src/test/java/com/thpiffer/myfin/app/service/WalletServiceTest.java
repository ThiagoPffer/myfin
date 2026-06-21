package com.thpiffer.myfin.app.service;

import com.thpiffer.myfin.app.dto.WalletCreateRequest;
import com.thpiffer.myfin.app.dto.WalletUpdateRequest;
import com.thpiffer.myfin.app.entity.BankEntity;
import com.thpiffer.myfin.app.entity.WalletEntity;
import com.thpiffer.myfin.app.enumeration.EnumWalletType;
import com.thpiffer.myfin.app.factory.WalletFactory;
import com.thpiffer.myfin.app.mapper.WalletMapper;
import com.thpiffer.myfin.app.repository.WalletRepository;
import com.thpiffer.myfin.app.service.impl.WalletServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {

    private final UUID WALLET_ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
    private final UUID BANK_ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");

    @InjectMocks
    private WalletServiceImpl service;
    @Mock
    private WalletRepository repository;
    @Mock
    private WalletFactory factory;
    @Mock
    private WalletMapper mapper;

    // ===================== GET ALL (Pagination and Filtering) =====================

    @Test
    void getWalletScrollPageByFilter_receivesPageAndFilter_callsRepository() {
        var page = PageRequest.of(0, 10);
        var filter = "any_filter";
        var sliceMock = Mockito.mock(Slice.class);
        var walletEntity = new WalletEntity();

        Mockito.when(sliceMock.hasNext()).thenReturn(true);
        Mockito.when(sliceMock.getContent()).thenReturn(List.of(walletEntity));
        Mockito.when(repository.findAll(filter, page)).thenReturn(sliceMock);

        var output = Assertions.assertDoesNotThrow(() ->
                service.getWalletScrollPageByFilter(page, filter));

        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.content());
        Assertions.assertTrue(output.hasNext());

        Mockito.verify(repository, Mockito.times(1)).findAll(filter, page);
    }

    @Test
    void getWalletScrollPageByFilter_receivesPageAndEmptyFilter_callsRepository() {
        var page = PageRequest.of(0, 10);
        var filter = "";
        var sliceMock = Mockito.mock(Slice.class);

        Mockito.when(sliceMock.hasNext()).thenReturn(false);
        Mockito.when(sliceMock.getContent()).thenReturn(List.of());
        Mockito.when(repository.findAll(filter, page)).thenReturn(sliceMock);

        var output = Assertions.assertDoesNotThrow(() ->
                service.getWalletScrollPageByFilter(page, filter));

        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.content());
        Assertions.assertTrue(output.content().isEmpty());
        Assertions.assertFalse(output.hasNext());

        Mockito.verify(repository, Mockito.times(1)).findAll(filter, page);
    }

    // ===================== GET BY ID =====================

    @Test
    void getWalletById_receivesValidId_returnsWalletEntityOptional() {
        var walletEntity = WalletEntity.builder()
                .id(WALLET_ID)
                .code(1001)
                .description("Wallet Test")
                .balance(new BigDecimal("1000.00"))
                .type(EnumWalletType.BANK_ACCOUNT)
                .build();

        Mockito.when(repository.findById(WALLET_ID)).thenReturn(Optional.of(walletEntity));

        var result = Assertions.assertDoesNotThrow(() -> service.getWalletById(WALLET_ID));

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(WALLET_ID, result.get().getId());
        Assertions.assertEquals(1001, result.get().getCode());

        Mockito.verify(repository, Mockito.times(1)).findById(WALLET_ID);
    }

    // ===================== CREATE =====================

    @Test
    void createWallet_receivesValidRequest_createsAndReturnsWalletEntity() {
        var request = new WalletCreateRequest(
                "Wallet Test",
                EnumWalletType.BANK_ACCOUNT,
                BANK_ID
        );
        var bank = BankEntity.builder().id(BANK_ID).build();
        var walletEntity = WalletEntity.builder()
                .id(WALLET_ID)
                .code(1001)
                .description("Wallet Test")
                .balance(new BigDecimal("1000.00"))
                .type(EnumWalletType.BANK_ACCOUNT)
                .bank(bank)
                .build();

        Mockito.when(factory.create(request)).thenReturn(walletEntity);
        Mockito.when(repository.save(Mockito.any(WalletEntity.class))).thenReturn(walletEntity);

        var result = Assertions.assertDoesNotThrow(() -> service.createWallet(request));

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1001, result.getCode());
        Assertions.assertEquals("Wallet Test", result.getDescription());
        Assertions.assertEquals(new BigDecimal("1000.00"), result.getBalance());

        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any(WalletEntity.class));
    }

    // ===================== UPDATE =====================

    @Test
    void updateWallet_receivesValidIdAndRequest_updatesAndReturnsWalletEntity() {
        var request = new WalletUpdateRequest(
                "Wallet Updated",
                EnumWalletType.INVESTMENT_ACCOUNT,
                BANK_ID
        );
        var bank = BankEntity.builder().id(BANK_ID).build();
        var existingWallet = WalletEntity.builder()
                .id(WALLET_ID)
                .code(1000)
                .description("Old Description")
                .balance(new BigDecimal("500.00"))
                .type(EnumWalletType.BANK_ACCOUNT)
                .bank(bank)
                .build();
        var updatedWallet = WalletEntity.builder()
                .id(WALLET_ID)
                .code(1001)
                .description("Wallet Updated")
                .balance(new BigDecimal("2000.00"))
                .type(EnumWalletType.INVESTMENT_ACCOUNT)
                .bank(bank)
                .build();

        Mockito.when(repository.findById(WALLET_ID)).thenReturn(Optional.of(existingWallet));
        Mockito.when(mapper.fromUpdateRequest(existingWallet, request)).thenReturn(updatedWallet);
        Mockito.when(repository.save(Mockito.any(WalletEntity.class))).thenReturn(updatedWallet);

        var result = Assertions.assertDoesNotThrow(() ->
                service.updateWallet(WALLET_ID, request));

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1001, result.getCode());
        Assertions.assertEquals("Wallet Updated", result.getDescription());
        Assertions.assertEquals(new BigDecimal("2000.00"), result.getBalance());

        Mockito.verify(repository, Mockito.times(1)).findById(WALLET_ID);
        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any(WalletEntity.class));
    }

    // ===================== DELETE =====================

    @Test
    void deleteWallet_receivesValidId_deletesWallet() {
        Mockito.doNothing().when(repository).deleteById(WALLET_ID);

        Assertions.assertDoesNotThrow(() -> service.deleteWallet(WALLET_ID));

        Mockito.verify(repository, Mockito.times(1)).deleteById(WALLET_ID);
    }

}

