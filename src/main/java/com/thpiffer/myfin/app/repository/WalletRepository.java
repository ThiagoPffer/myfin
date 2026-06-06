package com.thpiffer.myfin.app.repository;

import com.thpiffer.myfin.app.entity.WalletEntity;
import com.thpiffer.myfin.core.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WalletRepository extends BaseRepository<WalletEntity, UUID> {

    @Override
    default Class<WalletEntity> getEntityClass() {
        return WalletEntity.class;
    }

}
