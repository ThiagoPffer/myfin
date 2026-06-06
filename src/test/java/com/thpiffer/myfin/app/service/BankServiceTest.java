package com.thpiffer.myfin.app.service;

import com.thpiffer.myfin.app.entity.BankEntity;
import com.thpiffer.myfin.app.exception.NotFoundException;
import com.thpiffer.myfin.app.repository.BankRepository;
import com.thpiffer.myfin.app.service.impl.BankServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class BankServiceTest {

    @InjectMocks
    private BankServiceImpl service;
    @Mock
    private BankRepository repository;

    @Test
    void findBankById_receivesId_callsRepository() {
        var id = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        var bankEntity = new BankEntity();

        Mockito.when(repository.findById(id)).thenReturn(Optional.of(bankEntity));

        var output = Assertions.assertDoesNotThrow(() ->
                service.getBankById(id));

        Assertions.assertNotNull(output);

        Mockito.verify(repository, Mockito.times(1)).findById(id);
    }

    @Test
    void findBankById_receivesIdNotFound_callsRepository() {
        var id = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

        var exception = Assertions.assertThrows(NotFoundException.class, () ->
                service.getBankById(id));

        Assertions.assertNotNull(exception);
        Assertions.assertTrue(exception.getMessage().contains("Bank not found with id: " + id));

        Mockito.verify(repository, Mockito.times(1)).findById(id);
    }

    @Test
    void getBankScrollPageByFilter_receivesPageAndFilter_callsRepository() {
        var page = PageRequest.of(0, 10);
        var filter = "any_filter";
        var sliceMock = Mockito.mock(Slice.class);
        var bankEntity = new BankEntity();

        Mockito.when(sliceMock.hasNext()).thenReturn(true);
        Mockito.when(sliceMock.getContent()).thenReturn(List.of(bankEntity));
        Mockito.when(repository.findAll(filter, page)).thenReturn(sliceMock);

        var output = Assertions.assertDoesNotThrow(() ->
                service.getBankScrollPageByFilter(page, filter));

        Assertions.assertNotNull(output);
        Assertions.assertNotNull(output.content());
        Assertions.assertTrue(output.hasNext());

        Mockito.verify(repository, Mockito.times(1)).findAll(filter, page);
    }

}
