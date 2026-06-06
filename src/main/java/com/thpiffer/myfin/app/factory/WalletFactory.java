package com.thpiffer.myfin.app.factory;

import com.thpiffer.myfin.app.dto.WalletCreateRequest;
import com.thpiffer.myfin.app.entity.WalletEntity;

public interface WalletFactory {

    WalletEntity create(WalletCreateRequest request);

}

