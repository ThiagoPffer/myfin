package com.thpiffer.myfin.app.resource;

import com.thpiffer.myfin.app.dto.BankResponse;
import com.thpiffer.myfin.app.entity.BankEntity;
import com.thpiffer.myfin.app.mapper.BankMapper;
import com.thpiffer.myfin.app.resource.impl.BankResourceImpl;
import com.thpiffer.myfin.app.service.BankService;
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

import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class BankResourceTest {

    private final BankResponse DEFAULT_RESPONSE = new BankResponse(
            UUID.fromString("af5b5bc4-a6b8-49f6-aede-727e4986ae7c"),
            "Banco do Brasil",
            1
    );

    @InjectMocks
    private BankResourceImpl resource;
    @Mock
    private BankService service;
    @Mock
    private BankMapper mapper;

    @Test
    void getBankScrollPageByFilter_receivesValidRequest_returnsBankResponsePage() {
        var bankEntity = new BankEntity();
        var request = PagingRequest.of(0, 10);
        var pageRequest = PageRequest.of(request.page(), request.size());

        Mockito.when(service.getBankScrollPageByFilter(pageRequest, request.filter()))
                .thenReturn(new ScrollingOutput<>(true, List.of(bankEntity)));
        Mockito.when(mapper.toResponse(bankEntity)).thenReturn(DEFAULT_RESPONSE);

        ResponseEntity<List<BankResponse>> response = Assertions.assertDoesNotThrow(() ->
                resource.getBankScrollPageByFilter(request));

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertNotNull(response.getHeaders());
        Assertions.assertTrue(response.getHeaders().containsHeader("X-Has-Next"));

        Mockito.verify(service).getBankScrollPageByFilter(pageRequest, request.filter());
    }

    @Test
    void getBankScrollPageByFilter_receivesValidRequest_returnsEmptyBankResponsePage() {
        var request = PagingRequest.of(0, 10);
        var pageRequest = PageRequest.of(request.page(), request.size());

        Mockito.when(service.getBankScrollPageByFilter(pageRequest, request.filter()))
                .thenReturn(new ScrollingOutput<>(false, List.of()));

        ResponseEntity<List<BankResponse>> response = Assertions.assertDoesNotThrow(() ->
                resource.getBankScrollPageByFilter(request));

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertTrue(response.getBody().isEmpty());
        Assertions.assertNotNull(response.getHeaders());
        Assertions.assertTrue(response.getHeaders().containsHeader("X-Has-Next"));
        Assertions.assertEquals("false", response.getHeaders().getFirst("X-Has-Next"));

        Mockito.verify(service).getBankScrollPageByFilter(pageRequest, request.filter());
    }
}
