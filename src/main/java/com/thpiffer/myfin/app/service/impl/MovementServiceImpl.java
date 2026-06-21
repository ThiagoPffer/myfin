package com.thpiffer.myfin.app.service.impl;

import com.thpiffer.myfin.app.dto.MovementCreateRequest;
import com.thpiffer.myfin.app.dto.MovementUpdateRequest;
import com.thpiffer.myfin.app.entity.MovementEntity;
import com.thpiffer.myfin.app.entity.WalletEntity;
import com.thpiffer.myfin.app.exception.NotFoundException;
import com.thpiffer.myfin.app.mapper.MovementMapper;
import com.thpiffer.myfin.app.repository.MovementRepository;
import com.thpiffer.myfin.app.service.MovementService;
import com.thpiffer.myfin.app.service.WalletService;
import com.thpiffer.myfin.app.util.CompetenceUtils;
import com.thpiffer.myfin.core.dto.ScrollingOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MovementServiceImpl implements MovementService {

    private final MovementRepository repository;
    private final MovementMapper mapper;
    private final WalletService walletService;

    @Override
    public List<MovementEntity> getMovementListByCompetence(String competence) {
        LocalDate startDate = CompetenceUtils.getStartDateFromCompetenceString(competence);
        LocalDate endDate = CompetenceUtils.getEndDateFromCompetenceString(competence);
        return repository.findByMovementDateBetween(startDate, endDate);
    }

    @Override
    public ScrollingOutput<MovementEntity> getMovementScrollPageByFilter(PageRequest page, String filter) {
        Slice<MovementEntity> slice = repository.findAll(filter, page);
        return new ScrollingOutput<>(slice.hasNext(), slice.getContent());
    }

    @Override
    public Optional<MovementEntity> getMovementById(UUID id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public MovementEntity createMovement(MovementCreateRequest request) {
        WalletEntity wallet = walletService.getWalletById(request.walletId())
                .orElseThrow(() -> new NotFoundException("Wallet Not Found"));

        MovementEntity movementEntity = mapper.fromCreateRequest(request, wallet);

        return repository.save(movementEntity);
    }

    @Override
    @Transactional
    public MovementEntity updateMovement(UUID id, MovementUpdateRequest updateRequest) {
        var entity = repository.findById(id).orElseThrow
                (() -> new NotFoundException("Movement not found with id: " + id));

        WalletEntity wallet = walletService.getWalletById(updateRequest.walletId())
                .orElseThrow(() -> new NotFoundException("Wallet Not Found"));

        return repository.save(mapper.fromUpdateRequest(entity, updateRequest, wallet));
    }

    @Override
    @Transactional
    public void deleteMovementById(UUID id) {
        repository.deleteById(id);
    }

}
