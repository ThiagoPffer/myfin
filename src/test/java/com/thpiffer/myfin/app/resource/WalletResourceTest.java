package com.thpiffer.myfin.app.resource;

import com.thpiffer.myfin.app.dto.BankResponse;
import com.thpiffer.myfin.app.dto.WalletCreateRequest;
import com.thpiffer.myfin.app.dto.WalletResponse;
import com.thpiffer.myfin.app.dto.WalletUpdateRequest;
import com.thpiffer.myfin.app.entity.WalletEntity;
import com.thpiffer.myfin.app.enumeration.EnumWalletType;
import com.thpiffer.myfin.app.mapper.WalletMapper;
import com.thpiffer.myfin.app.resource.impl.WalletResourceImpl;
import com.thpiffer.myfin.app.service.WalletService;
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
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class WalletResourceTest {

    private final UUID DEFAULT_ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
    private final UUID BANK_ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");

    private final WalletResponse DEFAULT_RESPONSE = new WalletResponse(
            DEFAULT_ID,
            1001,
            "Wallet Test",
            new BigDecimal("1000.00"),
            EnumWalletType.BANK_ACCOUNT,
            new BankResponse(BANK_ID, "Bank Test", 123)
    );

    @InjectMocks
    private WalletResourceImpl resource;
    @Mock
    private WalletService service;
    @Mock
    private WalletMapper mapper;

    // ===================== GET ALL (Pagination and Filtering) =====================

    @Test
    void getWalletScrollPageByFilter_receivesValidRequest_returnsWalletResponsePage() {
        var walletEntity = new WalletEntity();
        var request = PagingRequest.of(0, 10);
        var pageRequest = PageRequest.of(request.page(), request.size());

        Mockito.when(service.getWalletScrollPageByFilter(pageRequest, request.filter()))
                .thenReturn(new ScrollingOutput<>(true, List.of(walletEntity)));
        Mockito.when(mapper.toResponse(walletEntity)).thenReturn(DEFAULT_RESPONSE);

        ResponseEntity<List<WalletResponse>> response = Assertions.assertDoesNotThrow(() ->
                resource.getWalletScrollPageByFilter(request));

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertNotNull(response.getHeaders());
        Assertions.assertTrue(response.getHeaders().containsHeader("X-Has-Next"));

        Mockito.verify(service).getWalletScrollPageByFilter(pageRequest, request.filter());
    }

    @Test
    void getWalletScrollPageByFilter_receivesValidRequest_returnsEmptyWalletResponsePage() {
        var request = PagingRequest.of(0, 10);
        var pageRequest = PageRequest.of(request.page(), request.size());

        Mockito.when(service.getWalletScrollPageByFilter(pageRequest, request.filter()))
                .thenReturn(new ScrollingOutput<>(false, List.of()));

        ResponseEntity<List<WalletResponse>> response = Assertions.assertDoesNotThrow(() ->
                resource.getWalletScrollPageByFilter(request));

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertTrue(response.getBody().isEmpty());
        Assertions.assertNotNull(response.getHeaders());
        Assertions.assertTrue(response.getHeaders().containsHeader("X-Has-Next"));
        Assertions.assertEquals("false", response.getHeaders().getFirst("X-Has-Next"));

        Mockito.verify(service).getWalletScrollPageByFilter(pageRequest, request.filter());
    }

    // ===================== GET BY ID =====================

    @Test
    void getWalletById_receivesValidId_returnsWalletResponse() {
        var walletEntity = new WalletEntity();

        Mockito.when(service.getWalletById(DEFAULT_ID)).thenReturn(walletEntity);
        Mockito.when(mapper.toResponse(walletEntity)).thenReturn(DEFAULT_RESPONSE);

        ResponseEntity<WalletResponse> response = Assertions.assertDoesNotThrow(() ->
                resource.getWalletById(DEFAULT_ID));

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(DEFAULT_ID, response.getBody().id());

        Mockito.verify(service).getWalletById(DEFAULT_ID);
        Mockito.verify(mapper).toResponse(walletEntity);
    }

    // ===================== CREATE =====================

    @Test
    void createWallet_receivesValidRequest_returnsCreatedWalletResponse() {
        var request = new WalletCreateRequest(
                "Wallet Test",
                EnumWalletType.BANK_ACCOUNT,
                BANK_ID
        );
        var walletEntity = new WalletEntity();

        Mockito.when(service.createWallet(request)).thenReturn(walletEntity);
        Mockito.when(mapper.toResponse(walletEntity)).thenReturn(DEFAULT_RESPONSE);

        ResponseEntity<WalletResponse> response = Assertions.assertDoesNotThrow(() ->
                resource.createWallet(request));

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.CREATED.value(), response.getStatusCode().value());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(DEFAULT_ID, response.getBody().id());

        Mockito.verify(service).createWallet(request);
        Mockito.verify(mapper).toResponse(walletEntity);
    }

    // ===================== UPDATE =====================

    @Test
    void updateWallet_receivesValidIdAndRequest_returnsUpdatedWalletResponse() {
        var request = new WalletUpdateRequest(
                "Wallet Updated",
                EnumWalletType.INVESTMENT_ACCOUNT,
                BANK_ID
        );
        var walletEntity = new WalletEntity();
        var updatedResponse = new WalletResponse(
                DEFAULT_ID,
                1001,
                "Wallet Updated",
                new BigDecimal("2000.00"),
                EnumWalletType.INVESTMENT_ACCOUNT,
                new BankResponse(BANK_ID, "Bank Test", 123)
        );

        Mockito.when(service.updateWallet(DEFAULT_ID, request)).thenReturn(walletEntity);
        Mockito.when(mapper.toResponse(walletEntity)).thenReturn(updatedResponse);

        ResponseEntity<WalletResponse> response = Assertions.assertDoesNotThrow(() ->
                resource.updateWallet(DEFAULT_ID, request));

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals("Wallet Updated", response.getBody().description());

        Mockito.verify(service).updateWallet(DEFAULT_ID, request);
        Mockito.verify(mapper).toResponse(walletEntity);
    }

    // ===================== DELETE =====================

    @Test
    void deleteWallet_receivesValidId_returnsNoContent() {
        Mockito.doNothing().when(service).deleteWallet(DEFAULT_ID);

        ResponseEntity<Void> response = Assertions.assertDoesNotThrow(() ->
                resource.deleteWallet(DEFAULT_ID));

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatusCode().value());

        Mockito.verify(service).deleteWallet(DEFAULT_ID);
    }

}

