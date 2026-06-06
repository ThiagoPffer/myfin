package com.thpiffer.myfin.app.factory;

import com.thpiffer.myfin.app.dto.WalletCreateRequest;
import com.thpiffer.myfin.app.entity.WalletEntity;
import com.thpiffer.myfin.app.enumeration.EnumWalletType;
import com.thpiffer.myfin.app.factory.impl.WalletFactoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class WalletFactoryTest {

    private final UUID BANK_ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");

    @InjectMocks
    private WalletFactoryImpl factory;

    @Test
    void create_receivesNullRequest_returnsNull() {
        WalletEntity result = factory.create(null);
        Assertions.assertNull(result, "Expected null when request is null");
    }

    @Test
    void create_receivesRequestWithZeroBalance_preservesBalance() {
        var request = new WalletCreateRequest(
                "Empty Wallet",
                EnumWalletType.VOUCHER,
                BANK_ID
        );

        WalletEntity result = factory.create(request);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(BigDecimal.ZERO, result.getBalance());
        Assertions.assertEquals("Empty Wallet", result.getDescription());
    }

    @Test
    void create_receivesRequestWithNullDescription_createsWalletWithNullDescription() {
        var request = new WalletCreateRequest(
                null,
                EnumWalletType.BANK_ACCOUNT,
                BANK_ID
        );

        WalletEntity result = factory.create(request);

        Assertions.assertNotNull(result);
        Assertions.assertNull(result.getDescription());
    }

    @Test
    void create_receivesNullBankId_createsWalletWithoutBank() {
        var request = new WalletCreateRequest(
                "No Bank Wallet",
                EnumWalletType.CASH,
                null
        );

        WalletEntity result = factory.create(request);

        Assertions.assertNotNull(result);
        Assertions.assertNull(result.getBank());
        Assertions.assertEquals("No Bank Wallet", result.getDescription());
    }

}
