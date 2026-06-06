package com.thpiffer.myfin.app.mapper;

import com.thpiffer.myfin.app.dto.BankResponse;
import com.thpiffer.myfin.app.entity.BankEntity;
import com.thpiffer.myfin.app.mapper.impl.BankMapperImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class BankMapperTest {

    private static final UUID DEFAULT_BANK_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    private static final String DEFAULT_BANK_NAME = "Banco de testes";
    private static final int DEFAULT_BANK_CODE = 124;

    @InjectMocks
    private BankMapperImpl mapper;

    @Test
    void toResponse_receivesNull_returnsNull () {
        Assertions.assertNull(mapper.toResponse(null));
    }

    @Test
    void toResponse_receivesBankEntity_returnsBankResponse() {
        BankEntity bankEntity = new BankEntity(DEFAULT_BANK_ID, DEFAULT_BANK_CODE, DEFAULT_BANK_NAME, null);

        BankResponse response = Assertions.assertDoesNotThrow(() -> mapper.toResponse(bankEntity));

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.id());
        Assertions.assertNotNull(response.name());
        Assertions.assertEquals(DEFAULT_BANK_ID, response.id());
        Assertions.assertEquals(DEFAULT_BANK_CODE, response.code());
        Assertions.assertEquals(DEFAULT_BANK_NAME, response.name());
    }

    @Test
    void fromId_receivesNull_returnsNull() {
        BankEntity result = Assertions.assertDoesNotThrow(() -> mapper.fromId(null));
        Assertions.assertNull(result);
    }

    @Test
    void fromId_receivesBankEntity_returnsBankEntityWithBankId() {
        BankEntity result = Assertions.assertDoesNotThrow(() -> mapper.fromId(DEFAULT_BANK_ID));

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getId());
        Assertions.assertEquals(DEFAULT_BANK_ID, result.getId());
    }

}
