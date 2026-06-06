package com.thpiffer.myfin.app.repository;

import com.thpiffer.myfin.app.entity.BankEntity;
import com.thpiffer.myfin.core.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BankRepository extends BaseRepository<BankEntity, UUID> {

    @Override
    default Class<BankEntity> getEntityClass() {
        return BankEntity.class;
    }

}
