package com.thpiffer.myfin.app.mapper;

import com.thpiffer.myfin.app.dto.BankResponse;
import com.thpiffer.myfin.app.dto.WalletResponse;
import com.thpiffer.myfin.app.dto.WalletUpdateRequest;
import com.thpiffer.myfin.app.entity.BankEntity;
import com.thpiffer.myfin.app.entity.WalletEntity;
import com.thpiffer.myfin.app.enumeration.EnumWalletType;
import com.thpiffer.myfin.app.mapper.impl.WalletMapperImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class WalletMapperTest {

    private static final UUID DEFAULT_WALLET_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    private static final int DEFAULT_WALLET_CODE = 1001;
    private static final String DEFAULT_WALLET_NAME = "Carteira de testes";

    private static final UUID DEFAULT_BANK_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174111");
    private static final String DEFAULT_BANK_NAME = "Banco de testes";
    private static final int DEFAULT_BANK_CODE = 124;

    @InjectMocks
    private WalletMapperImpl mapper;
    @Mock
    private BankMapper bankMapper;

    @Test
    public void toResponse_receivesNull_returnsNull() {
        WalletResponse response = Assertions.assertDoesNotThrow(
                () -> mapper.toResponse(null));
        Assertions.assertNull(response);
    }

    @Test
    public void toResponse_receivesWallet_returnsWallet() {
        BankEntity bankEntity = BankEntity.builder()
                .id(DEFAULT_BANK_ID)
                .name(DEFAULT_BANK_NAME)
                .code(DEFAULT_BANK_CODE)
                .build();

        WalletEntity walletEntity = WalletEntity.builder()
                .id(DEFAULT_WALLET_ID)
                .code(DEFAULT_WALLET_CODE)
                .description(DEFAULT_WALLET_NAME)
                .balance(BigDecimal.TEN)
                .type(EnumWalletType.BANK_ACCOUNT)
                .bank(bankEntity)
                .build();

        Mockito.when(bankMapper.toResponse(bankEntity)).thenReturn(
                new BankResponse(DEFAULT_BANK_ID, DEFAULT_BANK_NAME, DEFAULT_BANK_CODE)
        );

        WalletResponse response = Assertions.assertDoesNotThrow(() -> mapper.toResponse(walletEntity));

        Assertions.assertNotNull(response);
        Assertions.assertEquals(DEFAULT_WALLET_ID, response.id());
        Assertions.assertEquals(DEFAULT_WALLET_CODE, response.code());
        Assertions.assertEquals(DEFAULT_WALLET_NAME, response.description());
        Assertions.assertEquals(BigDecimal.TEN, response.balance());
        Assertions.assertEquals(EnumWalletType.BANK_ACCOUNT, response.type());
        Assertions.assertNotNull(response.bank());
        Assertions.assertEquals(DEFAULT_BANK_ID, response.bank().id());
        Assertions.assertEquals(DEFAULT_BANK_NAME, response.bank().name());
        Assertions.assertEquals(DEFAULT_BANK_CODE, response.bank().code());
    }

    @Test
    public void fromUpdateRequest_receivesNull_returnsNull() {
        WalletEntity entity = new WalletEntity();

        WalletEntity response = Assertions.assertDoesNotThrow(() ->
                mapper.fromUpdateRequest(entity, null));

        Assertions.assertNotNull(response);
    }

    @Test
    public void fromUpdateRequest_receivesValidRequest_returnsWallet() {
        UUID newBankId = UUID.fromString("123e4567-e89b-12d3-a456-426614174222");

        WalletEntity entity = WalletEntity.builder()
                .id(DEFAULT_WALLET_ID)
                .code(DEFAULT_WALLET_CODE)
                .description(DEFAULT_WALLET_NAME)
                .balance(BigDecimal.TEN)
                .type(EnumWalletType.BANK_ACCOUNT)
                .bank(BankEntity.builder().id(DEFAULT_BANK_ID).build())
                .build();

        WalletUpdateRequest request = new WalletUpdateRequest(
                "New Wallet", EnumWalletType.INVESTMENT_ACCOUNT, newBankId
        );

        Mockito.when(bankMapper.fromId(newBankId)).thenReturn(
                BankEntity.builder().id(newBankId).build()
        );

        WalletEntity result = Assertions.assertDoesNotThrow(() -> mapper.fromUpdateRequest(entity, request));

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBank());
        Assertions.assertEquals(entity.getId(), result.getId());
        Assertions.assertEquals(entity.getCode(), result.getCode());
        Assertions.assertEquals(entity.getBalance(), result.getBalance());
        Assertions.assertEquals(request.description(), result.getDescription());
        Assertions.assertEquals(request.bankId(), result.getBank().getId());
        Assertions.assertEquals(request.type(), result.getType());
    }

}
