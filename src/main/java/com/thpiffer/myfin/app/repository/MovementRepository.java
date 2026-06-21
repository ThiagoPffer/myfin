package com.thpiffer.myfin.app.repository;

import com.thpiffer.myfin.app.entity.MovementEntity;
import com.thpiffer.myfin.core.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface MovementRepository extends BaseRepository<MovementEntity, UUID> {

    @Override
    default Class<MovementEntity> getEntityClass() {
        return MovementEntity.class;
    }

    List<MovementEntity> findByMovementDateBetween(LocalDate startDate, LocalDate endDate);

}
